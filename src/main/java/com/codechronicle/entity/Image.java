package com.codechronicle.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Image {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String thumbUrl;
	private String webUrl;
	private String masterUrl;
	
	// This field should be indexed in the database for performance, since we frequently look things up by this.
	private String originalUrl;

	private boolean postProcessed = false;
	
	@ManyToMany(fetch=FetchType.LAZY)
	private Set<Tag> tags = new HashSet<Tag>();
	
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
	public boolean isPostProcessed() {
		return postProcessed;
	}
	public void setPostProcessed(boolean postProcessed) {
		this.postProcessed = postProcessed;
	}
	public String getMasterUrl() {
		return masterUrl;
	}
	public void setMasterUrl(String masterUrl) {
		this.masterUrl = masterUrl;
	}
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	
}
