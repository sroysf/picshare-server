package com.codechronicle.messaging;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisMessageQueue implements MessageQueue {

	private MessageEncoder messageEncoder;
	private JedisPool pool;
	
	public RedisMessageQueue() {
		pool = new JedisPool("localhost", 6379);
	}
	
	public MessageEncoder getMessageEncoder() {
		return messageEncoder;
	}

	public void setMessageEncoder(MessageEncoder messageEncoder) {
		this.messageEncoder = messageEncoder;
	}

	@Override
	public void enqueue(String queueName, AsyncMessage msg) {
		Jedis jedis = pool.getResource();
		try {
			jedis.rpush(queueName, messageEncoder.toEncodedString(msg));
		} catch (Throwable t) {
			throw new RuntimeException(t);
		} finally {
			pool.returnResource(jedis);
		}
	}

	@Override
	public <T> T dequeue(String queueName, Class<T> clazz) {
		Jedis jedis = pool.getResource();
		try {
			
			// This should block until data is available
			List<String> keyValue = jedis.blpop(0, queueName);
			if (keyValue != null) {
				T msg = messageEncoder.fromEncodedString(keyValue.get(1), clazz);
				return msg;
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		} finally {
			pool.returnResource(jedis);
		}
		
		return null;
	}

}
