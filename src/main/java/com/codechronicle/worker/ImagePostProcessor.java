package com.codechronicle.worker;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.codechronicle.messaging.AsyncMessage;
import com.codechronicle.messaging.MessageQueue;

public class ImagePostProcessor {
	
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("appContext.xml");
		
		MessageQueue queue = ctx.getBean("messageQueue", MessageQueue.class);
		
		for (int i=0; i < 5; i++) {
			AsyncMessage msg = queue.dequeue("testQueue", AsyncMessage.class);
			System.out.println("Got message #" + i + " -> " + msg);
		}
	}
}
