package com.codechronicle.picshare.server.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codechronicle.picshare.server.EnvironmentHelper;
import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.entity.Tag;
import com.codechronicle.picshare.server.messaging.MessageQueue;
import com.codechronicle.picshare.server.messaging.ProcessImageMessage;

@Controller
@RequestMapping(value="/rest")
public class RestController {
	
	private static Logger log = LoggerFactory.getLogger(RestController.class);

	@Resource(name="messageQueue")	
	private MessageQueue messageQueue;
	
	@PersistenceContext
	EntityManager em;
	
	/*@RequestMapping(method=RequestMethod.GET, value="/book/{bookId}")
	public @ResponseBody Book getBook (@PathVariable(value="bookId") long bookId, Model model) {
		
		log.info("Searching for book with id = " + bookId);
		Book book = new Book();
		book.setAuthor("JR Tolkein");
		book.setName("Fellowship of the Ring");
		
		return book;
	}*/
	
	@RequestMapping(method=RequestMethod.GET, value="/image/{id}")
	public @ResponseBody Image getImageInfoById(@PathVariable(value="id") Long id) {
		return em.find(Image.class, id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/images/{idlist}")
	public @ResponseBody List<Image> getImageInfoByIdList(@PathVariable(value="idlist") String idlist) {
		
		System.out.println("Testing idlist --> " + em);
		String[] ids = idlist.split(",");
		
		List<Long> numericIdList = new ArrayList<Long>();
		for (String id : ids) {
			numericIdList.add(Long.parseLong(id));
		}
		
		Query query = em.createQuery("select i from Image i where i.id IN (:ids)").setParameter("ids", numericIdList);
		List<Image> images = query.getResultList();
		return images;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/image")
	public @ResponseBody List<Image> getImageInfoByTag(@RequestParam(value="tag") String tag) {
		return em.createQuery("Select i from Image i join i.tags tag where tag.value = :tag").setParameter("tag", tag).getResultList();
	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST, value="/image")
	/**
	 * This can be exercised by hitting /test/form.
	 */
	public @ResponseBody Image postSingleImage (
			@RequestParam(value="origUrl") String originalUrl, 
			@RequestParam(value="hostOriginal",required=false) boolean hostOriginal,
			@RequestParam(value="tags",required=false) String tags) {
		
		
		Query query = em.createQuery("select i from Image i where originalUrl = :url");
		query.setParameter("url", originalUrl);
		List<Image> existingImage = query.getResultList();
		
		if (existingImage.size() > 0) {
			return existingImage.get(0);
		}
		
		Image image = new Image();
		image.setOriginalUrl(originalUrl);
		
		if ((tags != null) && (tags.length() > 0)) {
			String[] tagArray = tags.split("\\|");
			for (String tagStr : tagArray) {
				Tag tag = findOrCreateTag(tagStr);
				image.getTags().add(tag);
			}
		}
		
		em.persist(image);
		
		ProcessImageMessage msg = new ProcessImageMessage();
		msg.setImageId(image.getId());
		msg.setHostOriginal(hostOriginal);
		messageQueue.enqueue(EnvironmentHelper.PROCESS_IMAGE_QUEUE, msg);
		
		return image;
	}

	@Transactional
	private Tag findOrCreateTag(String tagStr) {
		Query query = em.createQuery("Select t from Tag t where value = :tag").setParameter("tag", tagStr);
		List<Tag> tags = query.getResultList();
		
		if (tags.size() > 0) {
			return tags.get(0);
		} else {
			Tag tag = new Tag(tagStr);
			em.persist(tag);
			return tag;
		}
	}
}