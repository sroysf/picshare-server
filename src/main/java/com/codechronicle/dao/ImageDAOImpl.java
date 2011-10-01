package com.codechronicle.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.codechronicle.entity.Image;
import com.codechronicle.entity.Tag;

public class ImageDAOImpl extends JpaDaoSupport implements ImageDAO {

	@PersistenceContext
	EntityManager em;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Image saveOrUpdate(Image image) {
		
		if (image.getId() == null) {
			getJpaTemplate().persist(image);
		} else {
			getJpaTemplate().merge(image);
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

	@Override
	public Image findById(long id) {
		return getJpaTemplate().find(Image.class, id);
	}
	
	@Override
	@Transactional
	public void deleteTag(final Tag tag) {
		
		final Tag tagToDelete = getJpaTemplate().find(Tag.class, tag.getId());
		
		getJpaTemplate().execute(new JpaCallback<Integer>() {
			@Override
			public Integer doInJpa(EntityManager em)
					throws PersistenceException {
				
				Query query = em.createNativeQuery("delete from image_tag where tags_id = :tagId");
				query.setParameter("tagId", tagToDelete.getId());
				return query.executeUpdate();
			}
		});
		
		getJpaTemplate().remove(tagToDelete);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Tag findOrCreateTag(String value) {
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("val", value);
		List<Tag> tags = getJpaTemplate().findByNamedParams("Select t from Tag t where value = :val", args);
		
		if (tags.size() > 0) {
			return tags.get(0);
		} else {
			Tag tag = new Tag(value);
			getJpaTemplate().persist(tag);
			return tag;
		}
	}
	
	@Override
	public List<Tag> getAllTags() {
		
		List<Tag> tags = getJpaTemplate().find("Select t from Tag t order by t.value desc");
		return tags;
	}
	
	@Override
	public List<Image> findImagesByTag(String tag) {
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("tag", tag);
		
		List<Image> images = getJpaTemplate().findByNamedParams("Select i from Image i join i.tags tag where tag.value = :tag", args); 
		return images;
	}
	
	@Override
	/**
	 * Returns a maximum of 1000 records at a time
	 */
	public List<Image> findImagesByProcessedStatus(final boolean status) {
		Map<String, Boolean> args = new HashMap<String, Boolean>();
		args.put("postProcessed", false);
		
		List<Image> images = getJpaTemplate().executeFind(new JpaCallback<List<Image>>() {
			@Override
			public List<Image> doInJpa(EntityManager em)
					throws PersistenceException {
				
				Query query = em.createQuery("Select i from Image i where i.postProcessed = :postProcessed");
				query.setParameter("postProcessed", status);
				query.setMaxResults(1000);
				
				return query.getResultList();
			}
		});
		
		return images;
	}
}
