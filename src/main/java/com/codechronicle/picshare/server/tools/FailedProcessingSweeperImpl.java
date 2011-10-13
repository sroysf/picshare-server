package com.codechronicle.picshare.server.tools;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.codechronicle.picshare.server.EnvironmentHelper;
import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.messaging.MessageQueue;
import com.codechronicle.picshare.server.messaging.ProcessImageMessage;

public class FailedProcessingSweeperImpl implements FailedProcessingSweeper {
	
	private static ApplicationContext context = null;
	private static Logger log = LoggerFactory.getLogger(FailedProcessingSweeperImpl.class);
	
	@PersistenceContext
	EntityManager em;
	
	@Resource(name="messageQueue")	
	private MessageQueue messageQueue;
	
	public static void main(String[] args) {
		EnvironmentHelper.configureEnvironment();

		context = new ClassPathXmlApplicationContext("appContext.xml");
		FailedProcessingSweeper sweeper = context.getBean("failedProcessingSweeper", FailedProcessingSweeper.class);
		sweeper.retryFailed();
	}
	
	@Override
	public void retryFailed() {
		List<Image> images = em.createQuery("select i from Image i where postProcessed = false").getResultList();
		System.out.println("Reprocessing images : " + images);
		
		for (Image image : images) {
			
			ProcessImageMessage msg = new ProcessImageMessage();
			msg.setImageId(image.getId());
			msg.setHostOriginal(false);  // TODO: Persist this flag in the images data model because we currently have no way to reconstruct the original intent
			
			System.out.println("Sending request to process image id = " + image.getId());
			messageQueue.enqueue(EnvironmentHelper.PROCESS_IMAGE_QUEUE, msg);
		}
	}
}
