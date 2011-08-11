package com.codechronicle.dao;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.codechronicle.entity.Image;

public class ImageDAOImpl extends JpaDaoSupport implements ImageDAO {

	@Override
	@Transactional
	public Image saveOrUpdate(Image image) {
		
		if (image.getId() == null) {
			getJpaTemplate().persist(image);
		}
		
		return image;
	}

}
