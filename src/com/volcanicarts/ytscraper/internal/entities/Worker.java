package com.volcanicarts.ytscraper.internal.entities;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import com.volcanicarts.ytscraper.api.entities.YTScraper;
import com.volcanicarts.ytscraper.api.entities.YTVideo;
import com.volcanicarts.ytscraper.internal.exceptions.InvalidVideoException;
import com.volcanicarts.ytscraper.internal.util.TimeUtil;

/**
 * A worker that makes a single request and scrape
 * @author VolcanicArts
 * @since 1.2.0
 */
public class Worker {
	
	private final Pattern CONFIG_PATTERN = Pattern.compile("\\{};ytplayer.config = (\\{.*?.*\\})");
	
	private URI uri;
	private YTScraper scraper;
	
	public void assign(URI uri, YTScraper scraper) {
		this.uri = uri;
		this.scraper = scraper;
	}
	
	public void run() {
		new Thread(() -> {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
				.url(Objects.requireNonNull(HttpUrl.get(Worker.this.uri)))
				.addHeader("Content-Type", "application/json")
				.build();

			try(Response res = client.newCall(request).execute()){
				if (res == null || res.code() >= 400) scraper.loadFailed(Worker.this, new InvalidVideoException(Worker.this.uri, "No data could be found for the provided video"));
				String body = Objects.requireNonNull(res.body()).string();

				Matcher m = CONFIG_PATTERN.matcher(body);
				if (m.find()) {
					JSONObject data = new JSONObject(m.group(1));
					JSONObject playerData = new JSONObject(data.getJSONObject("args").getString("player_response"));

					JSONObject videoDetails = playerData.getJSONObject("videoDetails");
					JSONObject playerMFR = playerData.getJSONObject("microformat").getJSONObject("playerMicroformatRenderer");

					YTVideo video = createVideo(videoDetails, playerMFR);
					if (video != null) scraper.videoLoaded(Worker.this, video);
				} else {
					scraper.loadFailed(Worker.this, new InvalidVideoException(Worker.this.uri, "No data could be found for the provided video"));
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				scraper.loadFailed(Worker.this, new InvalidVideoException(Worker.this.uri, "An exception has occured with the request"));
			}
		}).start();
		
	}
	
	private YTVideo createVideo(JSONObject videoDetails, JSONObject playerMFR) {
		YTVideoImpl video = new YTVideoImpl();
		video.setID(videoDetails.getString("videoId"));
		video.setTitle(videoDetails.getString("title"));
		video.setDuration(Long.parseLong(videoDetails.getString("lengthSeconds")) * 1000);
		video.setUploader(videoDetails.getString("author"));
		video.setUploaderID(videoDetails.getString("channelId"));
		video.setKeywords(videoDetails.getJSONArray("keywords").toList().toArray(new String[0]));
		long uploaded;
		try {
			uploaded = TimeUtil.parseUploaded(playerMFR.getString("publishDate"));
		} catch (ParseException e) {
			e.printStackTrace();
			scraper.loadFailed(this, new InvalidVideoException(this.uri, "Could not parse upload date correctly"));
			return null;
		}
		video.setUpload(uploaded);
		if (playerMFR.has("category")) video.setCategory(VideoCategory.valueOf(playerMFR.getString("category")));
		return video;
	}

}
