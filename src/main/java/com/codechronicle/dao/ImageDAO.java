package com.codechronicle.dao;

import java.util.List;

import com.codechronicle.entity.Image;

public interface ImageDAO {
	Image saveOrUpdate (Image image);
	
	List<Image> findByOrigUrl(String url);
}
