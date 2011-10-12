package com.codechronicle.picshare.server.worker;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.codechronicle.HttpConnectionHelper;
import com.codechronicle.picshare.server.EnvironmentHelper;
import com.codechronicle.picshare.server.dao.ImageDAO;
import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.entity.Tag;
import com.codechronicle.picshare.server.messaging.MessageQueue;
import com.codechronicle.picshare.server.messaging.ProcessImageMessage;
import com.codechronicle.picshare.server.storage.PersistentStoreProvider;

@Service
public class Tester {
	
	private static ApplicationContext context = null;
	private static Logger log = LoggerFactory.getLogger(Tester.class);
	
	private DefaultHttpClient client = new DefaultHttpClient();

	@Inject
	private ImageDAO imageDAO;
	
	@Inject
	MessageQueue messageQueue;
	
	
	public static void main(String[] args) {

		EnvironmentHelper.configureEnvironment();

		context = new ClassPathXmlApplicationContext("appContext.xml");
		
		Tester tester = context.getBean("tester", Tester.class);
		tester.doTest();
	}


	private void doTest() {
		String tag = "/nas/media/Pictures/2010/120.National Parks Tour";
		
		List<Image> imageList = imageDAO.findImagesByTag(tag, 50, 10);
		System.out.println("Length = " + imageList.size());
		
		for (Image image : imageList) {
			System.out.println(image.getId());
		}
	}
}
