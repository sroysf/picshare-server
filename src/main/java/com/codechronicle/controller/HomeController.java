package com.codechronicle.controller;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codechronicle.dao.ImageDAO;
import com.codechronicle.messaging.MessageQueue;

@Controller
@RequestMapping(value="/test")
public class HomeController {
	
	private static Logger log = LoggerFactory.getLogger(HomeController.class);

	@Inject
	private ImageDAO imageDAO;
	
	@Resource(name="messageQueue")	
	private MessageQueue messageQueue;
	
	@RequestMapping(method=RequestMethod.GET, value="/form")
	public String getTestForm() {
		return "testForm";
	}
}