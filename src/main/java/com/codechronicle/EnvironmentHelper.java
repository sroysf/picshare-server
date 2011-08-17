package com.codechronicle;

import java.net.URI;
import java.net.URISyntaxException;

public class EnvironmentHelper {
	
	public static final String PROCESS_IMAGE_QUEUE = "processImageQueue";
	
	public static void configureEnvironment() {

		configurePostgres();
		
		configureRedis();
	}

	private static void configureRedis() {
		String redisURL = System.getenv().get("REDISTOGO_URL");
		
		if ((redisURL == null) || ("".equals(redisURL))) {
			throw new RuntimeException("Unable to start. Requires env var REDISTOGO_URL to be set.");
		}
		
		String redisHost = redisURL.replaceAll("redis://(.*):(.*)@(.*):(.*)", "$3");
		String redisPort = redisURL.replaceAll("redis://(.*):(.*)@(.*):(.*)", "$4");
		String redisPassword = redisURL.replaceAll("redis://(.*):(.*)@(.*):(.*)", "$2");
		
		System.setProperty("redisHost", redisHost);
		System.setProperty("redisPort", redisPort);
		System.setProperty("redisPassword", redisPassword);
	}

	private static void configurePostgres() {
		String databaseURL = System.getenv().get("DATABASE_URL");
		
		if ((databaseURL == null) || ("".equals(databaseURL))) {
			throw new RuntimeException("Unable to start. Requires env var DATABASE_URL to be set.");
		}

		URI dbURI = null;
		
		try {
			dbURI = new URI(databaseURL);
		} catch (URISyntaxException e) {
			throw new RuntimeException("DATABASE_URL must be in proper URI format");
		}
		
		System.setProperty("jdbcURL", databaseURL.replaceAll(
				"postgres://(.*):(.*)@(.*)",
				"jdbc:postgresql://$3?user=$1&password=$2"));
	}

	public static String getAppContextRoot() {
		return "http://localhost:8080";
	}
}
