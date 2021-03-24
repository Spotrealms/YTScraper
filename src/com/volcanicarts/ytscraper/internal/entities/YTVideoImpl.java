package com.volcanicarts.ytscraper.internal.entities;

import java.util.Arrays;
import java.util.Date;

import com.volcanicarts.ytscraper.api.entities.YTVideo;
import com.volcanicarts.ytscraper.internal.util.TimeUtil;

import org.json.JSONObject;

/**
 * The YTVideo implementation class 
 * @author VolcanicArts
 * @since 1.0.0
 */
public class YTVideoImpl implements YTVideo {
	private String id;
	private String title;
	private String description;
	private long duration;

	private String uploader;
	private String uploaderID;

	private long uploadDate;
	private VideoCategory category;
	private String[] keywords;
	private String[] availableCountries;
	private long viewCount;
	private boolean ratingsEnabled;
	private long likeCount;
	private long dislikeCount;

	private boolean unlisted;
	private boolean crawlable;
	private boolean vprivate;
	private boolean ageRestricted;
	private boolean ytKids;
	private boolean liveContent;
	private boolean unpluggedCorpus;

	private JSONObject playerData;
	private JSONObject renderData;


	//GETTERS
	//Basic attributes
	@Override
	public String getID(){
		return id;
	}

	@Override
	public String getUrl(){
		return "https://www.youtube.com/watch?v=" + getID();
	}

	@Override
	public String getTitle(){
		return title;
	}

	@Override
	public String getDescription(){
		return description;
	}

	@Override
	public long getDuration(){
		return duration;
	}

	@Override
	public String getDurationFormatted(){
		return TimeUtil.convertMilliToHHMMSS(getDuration());
	}

	@Override
	public String[] getThumbnailUrls(){
		return Arrays.stream(ThumbnailQuality.values()).map(e ->
			ThumbnailQuality.URL_BASE + id + "/" + e.toString().toLowerCase() + ".jpg"
		).toArray(String[]::new);
	}

	@Override
	public String getThumbnailUrl(ThumbnailQuality quality){
		return ThumbnailQuality.URL_BASE + id + "/" + quality.toString().toLowerCase() + ".jpg";
	}

	@Override
	public String getThumbnailUrl(){
		return getThumbnailUrl(ThumbnailQuality.MQDEFAULT);
	}

	//Uploader attributes
	@Override
	public String getUploader(){
		return uploader;
	}

	@Override
	public String getUploaderID(){
		return uploaderID;
	}

	@Override
	public String getUploaderUrl(){
		return "https://www.youtube.com/channel/" + uploaderID;
	}

	//Additional metadata
	@Override
	public long getUploadDate(){
		return uploadDate;
	}

	@Override
	public String getUploadDateFormatted(){
		return new Date(getUploadDate()).toString();
	}

	@Override
	public VideoCategory getCategory(){
		return category;
	}

	@Override
	public String[] getKeywords(){
		return keywords;
	}

	@Override
	public String[] getAvailableCountries(){
		return availableCountries;
	}

	@Override
	public long getViewCount(){
		return viewCount;
	}

	@Override
	public String getViewCountFormatted(){
		return String.format("%,d", viewCount);
	}

	@Override public boolean isRatingsEnabled(){
		return ratingsEnabled;
	}

	@Override
	public long getLikes(){
		return ratingsEnabled ? likeCount : -1;
	}

	@Override
	public String getLikesFormatted(){
		return ratingsEnabled ? String.format("%,d", likeCount) : "-1";
	}

	@Override
	public long getDislikes(){
		return ratingsEnabled ? dislikeCount : -1;
	}

	@Override
	public String getDislikesFormatted(){
		return ratingsEnabled ? String.format("%,d", dislikeCount) : "-1";
	}

	@Override
	public double getLikeDislikeRatio(){
		//Check if ratings are enabled and they are imbalanced
		if(ratingsEnabled && likeCount != dislikeCount){
			if(likeCount > dislikeCount) return dislikeCount != 0 ? (double) likeCount / (double) dislikeCount : likeCount;
			else return likeCount != 0 ? -1 * ((double) dislikeCount / (double) likeCount) : dislikeCount;
		}
		else return 0;
	}

	@Override
	public String getLikeDislikeRatioFormatted(){
		return ratingsEnabled ? String.format("%.5g", getLikeDislikeRatio()) : "-1";
	}

	//Hidden attributes
	@Override
	public boolean isUnlisted(){
		return unlisted;
	}

	@Override
	public boolean isCrawlable(){
		return crawlable;
	}

	@Override
	public boolean isPrivate(){
		return vprivate;
	}

	@Override
	public boolean isAgeRestricted(){
		return ageRestricted;
	}

	@Override
	public boolean isYtKids(){
		return ytKids;
	}

	@Override
	public boolean isLiveContent(){
		return liveContent;
	}

	@Override
	public boolean isUnpluggedCorpus(){
		return unpluggedCorpus;
	}

	//Full JSON
	@Override
	public JSONObject getPlayerData(){
		return playerData;
	}

	@Override
	public JSONObject getRenderData(){
		return renderData;
	}

	//toString Override
	@Override
	public String toString(){
		return "{" + "id=" + getID() + ", " + "title=" + getTitle() + ", " + "description=" + getDescription() + ", " +
			"duration=" + getDurationFormatted() + ", " + "uploaderid=" + getUploaderID() + ", " +
			"uploader=" + getUploader() + ", " + "date=" + getUploadDateFormatted() + ", " +
			"category=" + getCategory() + ", " + "views=" + getViewCountFormatted() + ", " +
			"ratingson=" + isRatingsEnabled() + ", " + "likes=" + getLikesFormatted() + ", " +
			"dislikes=" + getDislikesFormatted() + ", " + "unlisted=" + isUnlisted() + ", " +
			"crawlable=" + isCrawlable() + ", " + "private=" + isPrivate() + ", " +
			"agerestricted=" + isAgeRestricted() + ", " + "ytkids=" + isYtKids() + ", " +
			"livecontent=" + isLiveContent() + ", " + "unpluggedcorpus=" + isUnpluggedCorpus() + "}";
	}


	//SETTERS
	//Basic attributes
	public YTVideoImpl setID(String id){
		this.id = id;
		return this;
	}
	
	public YTVideoImpl setTitle(String title){
		this.title = title;
		return this;
	}

	public YTVideoImpl setDescription(String description){
		this.description = description;
		return this;
	}
	
	public YTVideoImpl setDuration(long duration){
		this.duration = duration;
		return this;
	}

	//Uploader attributes
	public YTVideoImpl setUploader(String uploader){
		this.uploader = uploader;
		return this;
	}

	public YTVideoImpl setUploaderID(String uploaderID){
		this.uploaderID = uploaderID;
		return this;
	}

	//Additional metadata
	public YTVideoImpl setUploadDate(long uploadDate){
		this.uploadDate = uploadDate;
		return this;
	}
	
	public YTVideoImpl setCategory(VideoCategory category){
		this.category = category;
		return this;
	}

	public YTVideoImpl setKeywords(String[] keywords){
		this.keywords = keywords;
		return this;
	}

	public YTVideoImpl setAvailableCountries(String[] availableCountries){
		this.availableCountries = availableCountries;
		return this;
	}

	public YTVideoImpl setViewCount(long viewCount){
		this.viewCount = viewCount;
		return this;
	}

	public YTVideoImpl setIsRatingsAllowed(boolean ratingsEnabled){
		this.ratingsEnabled = ratingsEnabled;
		return this;
	}

	public YTVideoImpl setLikeCount(long likeCount){
		this.likeCount = likeCount;
		return this;
	}

	public YTVideoImpl setDislikeCount(long dislikeCount){
		this.dislikeCount = dislikeCount;
		return this;
	}

	//Hidden attributes
	public YTVideoImpl setIsUnlisted(boolean unlisted){
		this.unlisted = unlisted;
		return this;
	}

	public YTVideoImpl setIsCrawlable(boolean crawlable){
		this.crawlable = crawlable;
		return this;
	}

	public YTVideoImpl setIsPrivate(boolean vprivate){
		this.vprivate = vprivate;
		return this;
	}

	public YTVideoImpl setIsAgeRestricted(boolean ageRestricted){
		this.ageRestricted = ageRestricted;
		return this;
	}

	public YTVideoImpl setIsYtKids(boolean ytKids){
		this.ytKids = ytKids;
		return this;
	}

	public YTVideoImpl setIsLiveContent(boolean liveContent){
		this.liveContent = liveContent;
		return this;
	}

	public YTVideoImpl setIsUnpluggedCorpus(boolean unpluggedCorpus){
		this.unpluggedCorpus = unpluggedCorpus;
		return this;
	}

	//Full JSON
	public YTVideoImpl setPlayerData(JSONObject playerData){
		this.playerData = playerData;
		return this;
	}

	public YTVideoImpl setRenderData(JSONObject renderData){
		this.renderData = renderData;
		return this;
	}
}
