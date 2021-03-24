package test;

import com.volcanicarts.ytscraper.api.entities.YTScraper;
import com.volcanicarts.ytscraper.api.entities.YTVideo;
import com.volcanicarts.ytscraper.internal.entities.ThumbnailQuality;
import com.volcanicarts.ytscraper.internal.entities.VideoResultHandler;
import com.volcanicarts.ytscraper.internal.exceptions.InvalidVideoException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws URISyntaxException {
		// A simple test case for using the YTScraper and YTVideo classes
		URI[] uris = {
			new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"), //Rickroll
			new URI("https://www.youtube.com/watch?v=pBGmdxlYXTc"), //Unlisted
			new URI("https://www.youtube.com/watch?v=5xYet-wnqWo"), //Age restricted
			new URI("https://www.youtube.com/watch?v=pZw9veQ76fo"), //Yt kids
			new URI("https://www.youtube.com/watch?v=YbJOTdZBX1g"), //Video with negative ld ratio
			new URI("https://www.youtube.com/watch?v=DWcJFNfaw9c") //Live content
		};

		new YTScraper(Arrays.asList(uris)).load(new VideoResultHandler(){
			@Override
			public void videoLoaded(YTVideo video) {
				//Basic attributes
				System.out.println("ID: " + video.getID());
				System.out.println("URL: " + video.getUrl());
				System.out.println("Title: " + video.getTitle());
				System.out.println("Description: " + video.getDescription());
				System.out.println("Duration: " + video.getDuration());
				System.out.println("Duration (formatted): " + video.getDurationFormatted());
				System.out.println("Thumbnail URLs: " + Arrays.toString(video.getThumbnailUrls()));
				System.out.println("Thumbnail (maxres): " + video.getThumbnailUrl(ThumbnailQuality.MAXRESDEFAULT));
				System.out.println("Thumbnail (default): " + video.getThumbnailUrl());
				System.out.println();

				//Uploader attributes
				System.out.println("Uploader: " + video.getUploader());
				System.out.println("Uploader (by ID): " + video.getUploaderID());
				System.out.println("Uploader URL: " + video.getUploaderUrl());
				System.out.println();

				//Additional metadata
				System.out.println("Upload Date: " + video.getUploadDate());
				System.out.println("Upload Date (formatted): " + video.getUploadDateFormatted());
				System.out.println("Category: " + video.getCategory());
				System.out.println("Keywords: " + Arrays.toString(video.getKeywords()));
				System.out.println("Available Countries: " + Arrays.toString(video.getAvailableCountries()));
				System.out.println("View Count: " + video.getViewCount());
				System.out.println("View Count (formatted): " + video.getViewCountFormatted());
				System.out.println("Ratings Enabled: " + video.isRatingsEnabled());
				System.out.println("Likes & Dislikes: " + video.getLikes() + "::" + video.getDislikes() + " /// " + video.getLikeDislikeRatio());
				System.out.println("Likes & Dislikes (formatted): " + video.getLikesFormatted() + "::" + video.getDislikesFormatted() + " /// " + video.getLikeDislikeRatioFormatted());
				System.out.println();

				//Hidden attributes
				System.out.println("Unlisted: " + video.isUnlisted());
				System.out.println("Crawlable: " + video.isCrawlable());
				System.out.println("Private: " + video.isPrivate());
				System.out.println("Age Restricted: " + video.isAgeRestricted());
				System.out.println("In Yt KIds: " + video.isYtKids());
				System.out.println("Live Content: " + video.isLiveContent());
				System.out.println("Unplugged Corpus: " + video.isUnpluggedCorpus());
				System.out.println();
			}

			@Override
			public void loadFailed(InvalidVideoException e) {
				e.printStackTrace();
			}
		});
	}
}
