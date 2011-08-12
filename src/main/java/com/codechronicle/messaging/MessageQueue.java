package com.codechronicle.messaging;

public interface MessageQueue {

	void enqueue(String queueName, AsyncMessage msg);
	
	<T> T dequeue (String queueName, Class<T> clazz);
}
