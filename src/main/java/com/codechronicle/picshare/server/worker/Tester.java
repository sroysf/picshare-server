package com.codechronicle.picshare.server.worker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.codechronicle.picshare.server.EnvironmentHelper;
import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.messaging.MessageQueue;

@Service
public class Tester {
	
	private static ApplicationContext context = null;
	private static Logger log = LoggerFactory.getLogger(Tester.class);
	
	private DefaultHttpClient client = new DefaultHttpClient();

	@Inject
	MessageQueue messageQueue;
	
	@PersistenceContext
	EntityManager em;
	
	public static void main(String[] args) {

		EnvironmentHelper.configureEnvironment();

		context = new ClassPathXmlApplicationContext("appContext.xml");
		
		Tester tester = context.getBean("tester", Tester.class);
		tester.doTest();
	}


	private void doTest() {
		String idlist = "32579,32584,32588";
		String[] ids = idlist.split(",");
		List<Long> numericIdList = new ArrayList<Long>();
		for (String id : ids) {
			numericIdList.add(Long.parseLong(id));
		}
		
		Query query = em.createQuery("select i from Image i where i.id IN (:ids)").setParameter("ids", numericIdList);
		List<Image> images = query.getResultList();
		System.out.println(images);
	}
}
