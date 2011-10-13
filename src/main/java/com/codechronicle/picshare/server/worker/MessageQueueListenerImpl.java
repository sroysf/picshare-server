package com.codechronicle.picshare.server.worker;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.codechronicle.picshare.server.EnvironmentHelper;
import com.codechronicle.picshare.server.messaging.MessageQueue;
import com.codechronicle.picshare.server.messaging.ProcessImageMessage;

@Service
public class MessageQueueListenerImpl implements MessageQueueListener {

	private static ApplicationContext context = null;
	private static Logger log = LoggerFactory.getLogger(MessageQueueListenerImpl.class);
	
	@Inject
	MessageQueue messageQueue;
	
	@Inject
	ImagePostProcessor imagePostProcessor;
	
	public static void main(String[] args) {
		EnvironmentHelper.configureEnvironment();

		context = new ClassPathXmlApplicationContext("appContext.xml");
		
		MessageQueueListener listener = context.getBean("messageQueueListener", MessageQueueListener.class);
		listener.listenForMessages();
	}
	
	@Override
	public void listenForMessages() {
		int i=0;
		while (true) {
			i++;
			ProcessImageMessage msg = messageQueue.dequeue(EnvironmentHelper.PROCESS_IMAGE_QUEUE, ProcessImageMessage.class);
			log.info("Got message #" + i + ", image id = " + msg.getImageId());
			
			try {
				imagePostProcessor.processImage(msg.getImageId(), msg.isHostOriginal());
			} catch (Exception ex) {
				log.warn("Unexpected exception", ex);
				log.info("Skipping message and continuing processing...");
			}
		}
	}
}
