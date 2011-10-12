package com.codechronicle.picshare.server.messaging;

import java.util.List;

import org.eclipse.jetty.util.log.Log;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

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
		
		Jedis jedis = null; 
		do {
			Log.info("Refreshing Redis connection...");
			jedis = pool.getResource();
			
			try {
	
				List<String> keyValue = jedis.blpop(30, queueName);
				if (keyValue != null) {
					T msg = messageEncoder
							.fromEncodedString(keyValue.get(1), clazz);
					return msg;
				}
			} catch (JedisConnectionException jce) {
				if (jce.getMessage().contains("server has closed the connection")) {
					Log.warn("Redis server dropped blocking connection, reconnecting...");
				}
			} catch (Throwable t) {
				throw new RuntimeException(t);
			} finally {
				pool.returnResource(jedis);
			}
		} while (true);
	}

}
