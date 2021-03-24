package com.volcanicarts.ytscraper.api.entities;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.volcanicarts.ytscraper.api.entities.YTScraper;
import com.volcanicarts.ytscraper.internal.entities.VideoResultHandler;
import com.volcanicarts.ytscraper.internal.entities.Worker;
import com.volcanicarts.ytscraper.internal.exceptions.InvalidVideoException;

/**
 * The main class for scraping YouTube
 * @author VolcanicArts
 * @since 1.0.0
 */
public class YTScraper {
	
	private final int MAX_WORKERS = 5;
	
	private final List<URI> uris = new ArrayList<>();
	private final List<Worker> workers = new ArrayList<>();
	private VideoResultHandler handler;
	
	/**
	 * Makes a new YTScraper from a list of URIs
	 * @param uris The list of video URIs to get info for
	 */
	public YTScraper(List<URI> uris){
		this.uris.addAll(uris);
	}
	
	/**
	 * Makes a new YTScraper from a URI
	 * @param uri The URI of the video you want to get info on
	 */
	public YTScraper(URI uri) {
		this.uris.add(uri);
	}
	
	private Worker createWorker() {
		if (workers.size() == MAX_WORKERS) return null;
		Worker worker = new Worker();
		this.workers.add(worker);
		return worker;
	}
	
	/**
	 * Starts the worker process
	 */
	public void load(VideoResultHandler handler) {
		this.handler = handler;
		checkForComplete();
	}
	
	private void checkForComplete() {
		if (this.uris.size() != 0) {
			Worker worker = createWorker();
			if (worker != null) {
				URI uri = this.uris.remove(0);
				if (uri != null) {
					worker.assign(uri, this);
					worker.run();
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				checkForComplete();
			}
		}
	}
	
	public void videoLoaded(Worker worker, YTVideo video) {
		workers.remove(worker);
		this.handler.videoLoaded(video);
		checkForComplete();
	}
	
	public void loadFailed(Worker worker, InvalidVideoException e) {
		workers.remove(worker);
		this.handler.loadFailed(e);
		checkForComplete();
	}

}
