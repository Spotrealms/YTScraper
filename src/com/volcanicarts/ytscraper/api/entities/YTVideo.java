package com.volcanicarts.ytscraper.api.entities;

import com.volcanicarts.ytscraper.internal.entities.ThumbnailQuality;
import com.volcanicarts.ytscraper.internal.entities.VideoCategory;

import org.json.JSONObject;

/**
 * The interface for any returned YouTube video
 * @author VolcanicArts
 * @since 1.0.0
 */
public interface YTVideo {
	//Basic attributes
	/**
	 * @return The ID of the video
	 */
	String getID();

	/**
	 * @return The URL of the video
	 */
	String getUrl();

	/**
	 * @return The title of the video
	 */
	String getTitle();

	/**
	 * @return The description of the video
	 */
	String getDescription();

	/**
	 * @return The duration of the video in milliseconds
	 */
	long getDuration();
	
	/**
	 * @return The duration of the video in HH:MM:SS. Will exclude HH if 0
	 */
	String getDurationFormatted();

	/**
	 * @return A list of all the available thumbnail URLs
	 */
	String[] getThumbnailUrls();

	/**
	 * @return A specific thumbnail URL by quality
	 */
	String getThumbnailUrl(ThumbnailQuality quality);

	/**
	 * @return The default video thumbnail URL (assumes the user wants the "mqdefault" thumbnail)
	 */
	String getThumbnailUrl();


	//Uploader attributes
	/**
	 * @return The uploader by name
	 */
	String getUploader();

	/**
	 * @return The uploader by ID (classic ID or by name)
	 */
	String getUploaderID();

	/**
	 * @return The uploader's channel URL (by ID)
	 */
	String getUploaderUrl();


	//Additional metadata
	/**
	 * @return The upload date of the video in timecode format
	 */
	long getUploadDate();
	
	/**
	 * @return The upload date of the video in String format
	 */
	String getUploadDateFormatted();

	/**
	 * This is null in cases where the video author has not set a category
	 * @return The category of the video
	 */
	VideoCategory getCategory();

	/**
	 * @return A list of the keywords for the video
	 */
	String[] getKeywords();

	/**
	 * @return A list of the countries the video is allowed to be viewed in
	 */
	String[] getAvailableCountries();

	/**
	 * @return The amount of views the video has
	 */
	long getViewCount();

	/**
	 * @return The amount of views the video has in String format
	 */
	String getViewCountFormatted();

	/**
	 * @return Whether or not ratings are enabled
	 */
	boolean isRatingsEnabled();

	/**
	 * @return The amount of likes the video has
	 */
	long getLikes();

	/**
	 * @return The amount of likes the video has in String format
	 */
	String getLikesFormatted();

	/**
	 * @return The amount of dislikes the video has
	 */
	long getDislikes();

	/**
	 * @return The amount of dislikes the video has in String format
	 */
	String getDislikesFormatted();

	/**
	 * @return The ratio of likes to dislikes
	 */
	double getLikeDislikeRatio();

	/**
	 * @return The ratio of likes to dislikes in String format
	 */
	String getLikeDislikeRatioFormatted();


	//Hidden attributes
	/**
	 * @return Whether or not the video is unlisted
	 */
	boolean isUnlisted();

	/**
	 * @return Whether or not the video is crawlable by search engines
	 */
	boolean isCrawlable();

	/**
	 * @return Whether or not the video is private
	 */
	boolean isPrivate();

	/**
	 * @return Whether or not the video is age restricted
	 */
	boolean isAgeRestricted();

	/**
	 * @return Whether or not the video is available in YouTube Kids
	 */
	boolean isYtKids();

	/**
	 * @return Whether or not the video was live content
	 */
	boolean isLiveContent();

	/**
	 * @return The "unplugged corpus" status. Unsure of what this means
	 */
	boolean isUnpluggedCorpus();


	//Full JSON
	/**
	 * @return the player data JSON (contains video metadata)
	 */
	JSONObject getPlayerData();

	/**
	 * @return the render data JSON (contains info about how to render the content, including ratings)
	 */
	JSONObject getRenderData();
}
