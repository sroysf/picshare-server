package com.codechronicle.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codechronicle.EnvironmentHelper;
import com.codechronicle.dao.ImageDAO;
import com.codechronicle.entity.Book;
import com.codechronicle.entity.Image;
import com.codechronicle.entity.Tag;
import com.codechronicle.messaging.AsyncMessage;
import com.codechronicle.messaging.MessageQueue;
import com.codechronicle.messaging.ProcessImageMessage;

@Controller
@RequestMapping(value="/rest")
public class RestController {
	
	private static Logger log = LoggerFactory.getLogger(RestController.class);

	@Inject
	private ImageDAO imageDAO;
	
	@Resource(name="messageQueue")	
	private MessageQueue messageQueue;
	
	/*@RequestMapping(method=RequestMethod.GET, value="/book/{bookId}")
	public @ResponseBody Book getBook (@PathVariable(value="bookId") long bookId, Model model) {
		
		log.info("Searching for book with id = " + bookId);
		Book book = new Book();
		book.setAuthor("JR Tolkein");
		book.setName("Fellowship of the Ring");
		
		return book;
	}*/
	
	
	@RequestMapping(method=RequestMethod.POST, value="/image")
	/**
	 * This can be exercised by hitting /test/form.
	 */
	public @ResponseBody Image postSingleImage (
			@RequestParam(value="origUrl") String originalUrl, 
			@RequestParam(value="hostOriginal",required=false) boolean hostOriginal,
			@RequestParam(value="tags",required=false) String tags) {
		
		List<Image> existingImage = imageDAO.findByOrigUrl(originalUrl);
		if (existingImage.size() > 0) {
			return existingImage.get(0);
		}
		
		Image image = new Image();
		image.setOriginalUrl(originalUrl);
		
		if ((tags != null) && (tags.length() > 0)) {
			String[] tagArray = tags.split("\\|");
			for (String tagStr : tagArray) {
				Tag tag = imageDAO.findOrCreateTag(tagStr);
				image.getTags().add(tag);
			}
		}
		
		image = imageDAO.saveOrUpdate(image);
		
		ProcessImageMessage msg = new ProcessImageMessage();
		msg.setImageId(image.getId());
		msg.setHostOriginal(hostOriginal);
		messageQueue.enqueue(EnvironmentHelper.PROCESS_IMAGE_QUEUE, msg);
		
		return image;
	}
}