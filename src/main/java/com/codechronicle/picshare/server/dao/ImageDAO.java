package com.codechronicle.picshare.server.dao;

import java.util.List;

import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.entity.Tag;

public interface ImageDAO {
	
	Image saveOrUpdate (Image image);
	
	List<Image> findByOrigUrl(String url);
	
	Image findById(long id);
	
	void deleteTag(Tag tag);
	
	Tag findOrCreateTag(String value);
	
	List<Tag> getAllTags();
	
	List<Image> findImagesByTag(final String tag, final int firstResult, final int numResults);
	
	List<Image> findImagesByProcessedStatus(boolean status);
}