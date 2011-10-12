package com.codechronicle.picshare.server.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.codechronicle.picshare.server.dao.ImageDAO;
import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.entity.Tag;
import com.codechronicle.picshare.server.messaging.MessageQueue;

@Controller
@RequestMapping(value="/")
public class HomeController {
	
	private static Logger log = LoggerFactory.getLogger(HomeController.class);

	@Inject
	private ImageDAO imageDAO;
	
	@Resource(name="messageQueue")	
	private MessageQueue messageQueue;
	
	@RequestMapping(method=RequestMethod.GET, value="/test/form")
	public String getTestForm() {
		return "testForm";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/")
	public String getTags(Map<String, Object> model) {
		
		List<Tag> tagList = imageDAO.getAllTags();
		model.put("tagList", tagList);
		
		return "tags";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/image/thumbs")
	public String getThumbs(@RequestParam(value="tag")String tag, @RequestParam(value="start")int startNum, 
			@RequestParam(value="num")int numRecords, Map<String, Object> model) {
		
		log.info("Loading thumbnails for tag : " + tag);
		model.put("tag", tag);
		
		List<Image> imageList = imageDAO.findImagesByTag(tag, startNum, numRecords);
		model.put("imageList", imageList);
		
		int nextStart = startNum + 25;
		int prevStart = startNum - 25;
		boolean prevDisabled = false;
		boolean nextDisabled = false;
		
		if (prevStart < 0) {
			prevDisabled = true;
		}
		
		if (imageList.size() < 25) {
			nextDisabled = true;
		}
		
		model.put("nextStart", nextStart);
		model.put("prevStart", prevStart);
		model.put("prevDisabled", prevDisabled);
		model.put("nextDisabled", nextDisabled);
		
		return "thumbs";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/image/web")
	public String getThumbs(@RequestParam(value="id")Long id, Map<String, Object> model) {
		
		Image image = imageDAO.findById(id);
		model.put("image", image);
		
		return "view";
	}
}