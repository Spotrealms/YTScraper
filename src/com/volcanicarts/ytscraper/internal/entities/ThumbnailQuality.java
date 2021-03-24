package com.volcanicarts.ytscraper.internal.entities;

/**
 * Defines the quality of the thumbnails.
 *
 * @since 1.3.1
 */
public enum ThumbnailQuality {
	/** Standard definition, lowest quality. */
	SDDEFAULT,

	/** Medium definition, medium quality. */
	MQDEFAULT,

	/** High definition, high quality. */
	HQDEFAULT,

	/** Native resolution, highest quality possible. */
	MAXRESDEFAULT;

	/** The base URL for thumbnails. */
	public static final String URL_BASE = "https://img.youtube.com/vi/";
}
