package com.codechronicle.dao;

import java.util.List;

import com.codechronicle.entity.Image;
import com.codechronicle.entity.Tag;

public interface ImageDAO {
	
	Image saveOrUpdate (Image image);
	
	List<Image> findByOrigUrl(String url);
	
	Image findById(long id);
	
	Image findByIdWithTags(long id);
	
	void deleteTag(Tag tag);
	
	Tag findOrCreateTag(String value);
}