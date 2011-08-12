package com.codechronicle.worker;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.codechronicle.EnvironmentHelper;
import com.codechronicle.messaging.AsyncMessage;
import com.codechronicle.messaging.MessageQueue;

public class ImagePostProcessor {

	public static void main(String[] args) {

		EnvironmentHelper.configureEnvironment();

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("appContext.xml");

		MessageQueue queue = ctx.getBean("messageQueue", MessageQueue.class);

		int i=0;
		while (true) {
			i++;
			AsyncMessage msg = queue.dequeue("testQueue", AsyncMessage.class);
			System.out.println("Got message #" + i + " -> " + msg);
		}
	}
}
