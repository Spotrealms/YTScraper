package com.volcanicarts.ytscraper.internal.entities;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.volcanicarts.ytscraper.api.entities.YTScraper;
import com.volcanicarts.ytscraper.api.entities.YTVideo;
import com.volcanicarts.ytscraper.internal.exceptions.InvalidVideoException;
import com.volcanicarts.ytscraper.internal.util.TimeUtil;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A worker that makes a single request and scrape
 * @author VolcanicArts
 * @since 1.2.0
 */
public class Worker {
	//private final Pattern CONFIG_PATTERN = Pattern.compile("var ytInitialData = (\\{.*?.*\\})");
	private final Pattern PLAYER_DATA_PATTERN = Pattern.compile("<script nonce=\".*\">var ytInitialPlayerResponse = (\\{.*?.*})");
	private final Pattern INITIAL_DATA_PATTERN = Pattern.compile("<script nonce=\".*\">var ytInitialData = (\\{.*?.*})");

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
				.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
				.build();

			try(Response res = client.newCall(request).execute()){
				if(res == null || res.code() >= 400) scraper.loadFailed(Worker.this, new InvalidVideoException(Worker.this.uri, "No data could be found for the provided video"));
				String body = Objects.requireNonNull(res.body()).string();

				//Create the JSONObjects for the data
				JSONObject playerData = new JSONObject();
				JSONObject initialData = new JSONObject();

				//Get the JSON for the initial player config
				Matcher configDataMatcher = PLAYER_DATA_PATTERN.matcher(body);
				if(configDataMatcher.find()) playerData = new JSONObject(configDataMatcher.group(1));
				Matcher initialDataMatcher = INITIAL_DATA_PATTERN.matcher(body);
				if(initialDataMatcher.find()) initialData = getNestedJsonChainJO(new JSONObject(initialDataMatcher.group(1)), "$.contents.twoColumnWatchNextResults.results.results");

				//Check if either of the JSONObjects are empty
				if(playerData.isEmpty() || initialData.isEmpty()){
					scraper.loadFailed(Worker.this, new InvalidVideoException(Worker.this.uri, "No data could be found for the provided video"));
				}

				//Create the video
				YTVideo video = createVideo(playerData, initialData);
				if (video != null) scraper.videoLoaded(Worker.this, video);
			}
			catch(IOException e){
				e.printStackTrace();
				scraper.loadFailed(Worker.this, new InvalidVideoException(Worker.this.uri, "An exception has occurred with the request"));
			}
		}).start();
	}
	
	private YTVideo createVideo(JSONObject playerData, JSONObject initialData){
		//Create a new video object
		YTVideoImpl video = new YTVideoImpl();

		//Get each major data section on their own for faster processing
		JSONObject videoDetails = playerData.getJSONObject("videoDetails");
		JSONObject playerMFR = playerData.getJSONObject("microformat").getJSONObject("playerMicroformatRenderer");

		//Basic attributes
		video.setID(videoDetails.getString("videoId"));
		video.setTitle(videoDetails.getString("title"));
		if(playerMFR.has("description")) video.setDescription(playerMFR.getJSONObject("description").getString("simpleText"));
		video.setDuration(videoDetails.getLong("lengthSeconds") * 1000);

		//Uploader attributes
		video.setUploader(videoDetails.getString("author"));
		video.setUploaderID(videoDetails.getString("channelId"));

		//Additional metadata
		long uploaded;
		try {
			uploaded = TimeUtil.parseUploaded(playerMFR.getString("publishDate"));
		}
		catch(ParseException e){
			e.printStackTrace();
			scraper.loadFailed(this, new InvalidVideoException(this.uri, "Could not parse upload date correctly"));
			return null;
		}
		video.setUploadDate(uploaded);
		if(playerMFR.has("category")) video.setCategory(VideoCategory.valueOfByName(playerMFR.getString("category")));
		if(videoDetails.has("keywords")) video.setKeywords(videoDetails.getJSONArray("keywords").toList()
			.stream().map(Object::toString).toArray(String[]::new));
		if(playerMFR.has("availableCountries")) video.setAvailableCountries(playerMFR.getJSONArray("availableCountries").toList()
			.stream().map(Object::toString).toArray(String[]::new));
		video.setViewCount(videoDetails.getLong("viewCount"));
		video.setIsRatingsAllowed(videoDetails.getBoolean("allowRatings"));
		//Likes & Dislikes
		if(nestedJsonChainExists(initialData, "$.contents[0].videoPrimaryInfoRenderer.sentimentBar.sentimentBarRenderer.tooltip")){
			String[] likesDislikes = getNestedJsonChainJO(initialData, "$.contents[0].videoPrimaryInfoRenderer.sentimentBar.sentimentBarRenderer")
				.getString("tooltip").replaceAll("[, ]", "").split("/");

			video.setLikeCount(Long.parseLong(likesDislikes[0]));
			video.setDislikeCount(Long.parseLong(likesDislikes[1]));
		}


		//Hidden attributes
		video.setIsUnlisted(playerMFR.getBoolean("isUnlisted"));
		video.setIsCrawlable(videoDetails.getBoolean("isCrawlable"));
		video.setIsPrivate(videoDetails.getBoolean("isPrivate"));
		video.setIsAgeRestricted(!playerMFR.getBoolean("isFamilySafe"));
		if(playerData.getJSONObject("playabilityStatus").has("miniplayer")) video.setIsYtKids(isYtKidsVideoByMiniplayer(playerData.getJSONObject("playabilityStatus").getJSONObject("miniplayer")));
		video.setIsLiveContent(videoDetails.getBoolean("isLiveContent"));
		video.setIsUnpluggedCorpus(videoDetails.getBoolean("isUnpluggedCorpus"));


		//Full JSON
		video.setPlayerData(playerData);
		video.setRenderData(initialData);

		return video;
	}

	/**
	 * Detects if a video is a YouTube kids video
	 * via the "miniplayer denied" method
	 * @param miniplayerJson The JSON data for the miniplayer
	 * @return Whether or not the video is available in YouTube Kids
	 */
	private boolean isYtKidsVideoByMiniplayer(JSONObject miniplayerJson){
		//Check if the playability status is anything other than "allow"
		if(!miniplayerJson.getJSONObject("miniplayerRenderer").getString("playbackMode").equals("PLAYBACK_MODE_ALLOW")){
			final String nest = "$.minimizedEndpoint.openPopupAction.popup.notificationActionRenderer.responseText";

			//Get the response, if it exists
			if(nestedJsonChainExists(miniplayerJson.getJSONObject("miniplayerRenderer"), nest)){
				//Get the nested miniplayer text
				String miniplayerText = getNestedJsonChainJO(miniplayerJson.getJSONObject("miniplayerRenderer"), nest).getString("simpleText");

				//Check if it contains the word kid and return true, as the miniplayer issues are definitely YouTube Kids related
				return miniplayerText.toLowerCase().contains("kids");
			}
		}

		//YouTube Kids is not preventing the miniplayer from working or the issue is unrelated, so return false
		return false;
	}

	/**
	 * Checks if a nested sequence of JSON objects exist given a path.
	 * @param object The object to search
	 * @param path The dot-delimited path to check for
	 * @return Whether or not the path exists in the object
	 */
	private boolean nestedJsonChainExists(JSONObject object, String path){
		//Derive a ReadContext for the object
		ReadContext ctx = JsonPath.parse(object.toString());

		//Attempt to derive an object from the ReadContext
		try {
			//Read the path and attempt to derive an object, returning true if the object isn't null
			return ctx.read(path) != null;
		}
		catch(PathNotFoundException ignored){
			//Return false, as the path doesn't exist
			return false;
		}
	}

	/**
	 * Gets a nested JSON Object/Array given a path.
	 * @param object The object to search
	 * @param path The dot-delimited path to get
	 * @return The nested JSON Object or JSON Array at the referenced path as a string
	 */
	private <T> LinkedHashMap<T, T> getNestedJsonChain(JSONObject object, String path){
		//Derive a ReadContext for the object
		ReadContext ctx = JsonPath.parse(object.toString());

		//Attempt to derive an object from the ReadContext
		try {
			//Read the path and attempt to derive an object, returning it if successful
			return ctx.read(path);
		}
		catch(PathNotFoundException ignored){
			//Return a blank JSONObject
			return new LinkedHashMap<>();
		}
	}

	/**
	 * Gets a nested JSON Object given a path.
	 * @param object The object to search
	 * @param path The dot-delimited path to get
	 * @return The nested JSONObject at the referenced path as a string
	 */
	private <T> JSONObject getNestedJsonChainJO(JSONObject object, String path){
		return new JSONObject(getNestedJsonChain(object, path));
	}
}
