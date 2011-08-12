package com.codechronicle.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codechronicle.dao.ImageDAO;
import com.codechronicle.entity.Book;
import com.codechronicle.entity.Image;

@Controller
@RequestMapping(value="/rest")
public class RestController {
	
	private static Logger log = LoggerFactory.getLogger(RestController.class);

	@Inject
	private ImageDAO imageDAO;
	
	@RequestMapping(method=RequestMethod.GET, value="/book/{bookId}")
	public @ResponseBody Book getBook (@PathVariable(value="bookId") long bookId, Model model) {
		
		log.info("Searching for book with id = " + bookId);
		Book book = new Book();
		book.setAuthor("JR Tolkein");
		book.setName("Fellowship of the Ring");
		
		return book;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/image")
	public @ResponseBody Image postSingleImage () {
		
		String localPath = "/home/sroy/temp/ajcook.jpg";
		String origUrl   = "https://homenas/nas/media/Pictures/2010/150.Kai%20Sept/DSC_6494.JPG";

		// Check to see if we have this already. If we do, just retrieve the record and send it back.
		List<Image> existingImages = imageDAO.findByOrigUrl(origUrl);
		if (existingImages.size() > 0) {
			return existingImages.get(0);
		}
		
		// Otherwise, create a new image, save the record, and then queue up post processing.
		Image image = new Image();
		image.setLocalPath(localPath);
		image.setOriginalUrl(origUrl);
		
		image = imageDAO.saveOrUpdate(image);
		
		return image;
	}
}