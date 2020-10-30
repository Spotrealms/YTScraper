package com.volcanicarts.ytscraper.internal.entities;

import java.util.Date;

import com.volcanicarts.ytscraper.api.entities.YTVideo;
import com.volcanicarts.ytscraper.internal.util.TimeUtil;

/**
 * The YTVideo implementation class 
 * @author VolcanicArts
 * @since 1.0.0
 */
public class YTVideoImpl implements YTVideo {
	
	private String ID;
	private String title;
	private long duration;
	private long upload;
	private VideoCategory category;
	private String uploader;
	private String uploaderID;
	private String[] keywords;
	
	public YTVideoImpl setID(String ID) {
		this.ID = ID;
		return this;
	}
	
	public YTVideoImpl setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public YTVideoImpl setDuration(long duration) {
		this.duration = duration;
		return this;
	}
	
	public YTVideoImpl setUpload(long upload) {
		this.upload = upload;
		return this;
	}
	
	public YTVideoImpl setCategory(VideoCategory category) {
		this.category = category;
		return this;
	}

	public YTVideoImpl setUploader(String uploader) {
		this.uploader = uploader;
		return this;
	}

	public YTVideoImpl setUploaderID(String uploaderID) {
		this.uploaderID = uploaderID;
		return this;
	}

	public YTVideoImpl setKeywords(String[] keywords) {
		this.keywords = keywords;
		return this;
	}

	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public String getURL() {
		return "https://www.youtube.com/watch?v=" + getID();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public long getDuration() {
		return duration;
	}
	
	@Override
	public String getDurationFormatted() {
		return TimeUtil.convertMilliToHHMMSS(getDuration());
	}

	@Override
	public long getUpload() {
		return upload;
	}
	
	@Override
	public String getUploadFormatted() {
		return new Date(getUpload()).toString();
	}

	@Override
	public VideoCategory getCategory() {
		return category;
	}

	@Override
	public String getUploader(){
		return uploader;
	}

	@Override
	public String getUploaderID(){
		return uploaderID;
	}

	@Override
	public String[] getKeywords(){
		return keywords;
	}
}
