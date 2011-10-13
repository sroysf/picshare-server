package com.codechronicle.picshare.server.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.entity.Tag;

@Service
public class TransactionalDAOImpl implements TransactionalDAO {

	@PersistenceContext
	EntityManager em;
	
	@Override
	@Transactional
	public Image saveOrUpdateImage(Image image) {
		if (image.getId() == null) {
			em.persist(image);
		} else {
			em.merge(image);
		}
		
		return image;
	}
	
	@Override
	@Transactional
	public Tag saveOrUpdateTag(Tag tag) {
		if (tag.getId() == null) {
			em.persist(tag);
		} else {
			em.merge(tag);
		}
		
		return tag;
	}
}
