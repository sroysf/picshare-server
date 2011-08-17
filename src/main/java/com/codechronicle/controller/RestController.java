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
import org.springframework.web.bind.annotation.ResponseBody;

import com.codechronicle.EnvironmentHelper;
import com.codechronicle.dao.ImageDAO;
import com.codechronicle.entity.Book;
import com.codechronicle.entity.Image;
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
	
	/*@RequestMapping(method=RequestMethod.GET, value="/msg")
	public @ResponseBody AsyncMessage getMessage () {
		return messageQueue.dequeue("testQueue", AsyncMessage.class);
	}*/
	
	@RequestMapping(method=RequestMethod.GET, value="/image")
	public @ResponseBody Image postSingleImage () {
		
		//String origUrl = "http://www.citypictures.net/data/media/227/Monch_and_Eiger_Grosse_Scheidegg_Switzerland.jpg";
		String origUrl = "http://www.zastavki.com/pictures/1024x768/2008/Movies_Movies_U_Underworld__Evolution_010690_.jpg";

		// Check to see if we have this already. If we do, just retrieve the record and send it back.
		List<Image> existingImages = imageDAO.findByOrigUrl(origUrl);
		if (existingImages.size() > 0) {
			Image existingImage = existingImages.get(0);
			log.info("Requested image already in database, id = " + existingImage.getId() + ", original URL = " + existingImage.getOriginalUrl());
			return existingImages.get(0);
		}
		
		// Otherwise, create a new image, save the record, and then queue up post processing.
		Image image = new Image();
		image.setLocalPath(null);
		image.setOriginalUrl(origUrl);
		image = imageDAO.saveOrUpdate(image);
		
		ProcessImageMessage msg = new ProcessImageMessage();
		msg.setImageId(image.getId());
		messageQueue.enqueue(EnvironmentHelper.PROCESS_IMAGE_QUEUE, msg);
		
		return image;
	}
}