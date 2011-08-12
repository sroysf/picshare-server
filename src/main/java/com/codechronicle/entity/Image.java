package com.codechronicle.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Image {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String localPath;
	private String thumbUrl;
	private String webUrl;
	
	// This field should be indexed in the database for performance, since we frequently look things up by this.
	private String originalUrl;
	
	private boolean postProcessed = false;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public boolean isPostProcessed() {
		return postProcessed;
	}
	public void setPostProcessed(boolean postProcessed) {
		this.postProcessed = postProcessed;
	}
}
