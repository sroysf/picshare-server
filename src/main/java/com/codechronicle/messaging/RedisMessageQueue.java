package com.codechronicle.messaging;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisMessageQueue implements MessageQueue {

	private MessageEncoder messageEncoder;
	private JedisPool pool;

	public RedisMessageQueue() {

		JedisPoolConfig config = new JedisPoolConfig();
		config.setTestOnBorrow(false);
		pool = new JedisPool(config, System.getProperty("redisHost"),
				Integer.parseInt(System.getProperty("redisPort")), 2000,
				System.getProperty("redisPassword"));
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
		
		// TODO: Add reconnect capability if server closes connection due to blocking for too long
		
		Jedis jedis = pool.getResource();
		try {

			// This should block until data is available
			List<String> keyValue = jedis.blpop(0, queueName);
			if (keyValue != null) {
				T msg = messageEncoder
						.fromEncodedString(keyValue.get(1), clazz);
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
