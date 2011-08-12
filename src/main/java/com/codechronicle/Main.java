package com.codechronicle;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.codechronicle.messaging.AsyncMessage;
import com.codechronicle.messaging.JSONMessageEncoder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * This class launches the web application in an embedded Jetty container. This
 * is the entry point to your application. The Java command that is used for
 * launching should fire this main method.
 * 
 */
public class Main {

	/*public static void main(String[] args) {
		JedisPool pool = new JedisPool("localhost", 6379);
		Jedis jedis = pool.getResource();
		try {
			
			AsyncMessage roy = new AsyncMessage("Saptarshi", 35);
			AsyncMessage malini = new AsyncMessage("Malini", 26);
			
			jedis.rpush("myqueue", JSONMessageEncoder.toJSON(roy));
			jedis.rpush("myqueue", JSONMessageEncoder.toJSON(malini));
			
			String firstpop = jedis.lpop("myqueue");
			String secondpop = jedis.lpop("myqueue");
			String thirdpop = jedis.lpop("myqueue");
			
			System.out.println(firstpop);
			System.out.println(secondpop);
			System.out.println(thirdpop);

		} finally {
			// / ... it's important to return the Jedis instance to the pool
			// once you've finished using it
			pool.returnResource(jedis);
		}
		// / ... when closing your application:
		pool.destroy();
	}*/

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String webappDirLocation = "src/main/webapp/";

		configureEnvironment();

		// The port that we should run on can be set into an environment
		// variable
		// Look for that variable and default to 8080 if it isn't there.
		String webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}

		Server server = new Server(Integer.valueOf(webPort));
		WebAppContext root = new WebAppContext();

		root.setContextPath("/");
		root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);

		// Parent loader priority is a class loader setting that Jetty accepts.
		// By default Jetty will behave like most web containers in that it will
		// allow your application to replace non-server libraries that are part
		// of the
		// container. Setting parent loader priority to true changes this
		// behavior.
		// Read more here:
		// http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
		root.setParentLoaderPriority(true);

		server.setHandler(root);

		server.start();
		server.join();
	}

	private static void configureEnvironment() {

		String databaseURL = System.getenv().get("DATABASE_URL");

		System.setProperty("jdbcURL", databaseURL.replaceAll(
				"postgres://(.*):(.*)@(.*)",
				"jdbc:postgresql://$3?user=$1&password=$2"));
	}

}
