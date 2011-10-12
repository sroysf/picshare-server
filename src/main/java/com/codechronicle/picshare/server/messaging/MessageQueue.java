package com.codechronicle.picshare.server.messaging;

public interface MessageQueue {

	void enqueue(String queueName, AsyncMessage msg);
	
	<T> T dequeue (String queueName, Class<T> clazz);
}
