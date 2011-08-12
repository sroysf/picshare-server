package com.codechronicle.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.codechronicle.entity.Image;

public class ImageDAOImpl extends JpaDaoSupport implements ImageDAO {

	@PersistenceContext
	EntityManager em;
	
	@Override
	@Transactional
	public Image saveOrUpdate(Image image) {
		
		if (image.getId() == null) {
			getJpaTemplate().persist(image);
		}
		
		return image;
	}
	
	@Override
	public List<Image> findByOrigUrl(String url) {
		
		Query query = em.createQuery("select i from Image i where originalUrl = :url");
		query.setParameter("url", url);
		List<Image> images = query.getResultList();
		return images;
	}

}
