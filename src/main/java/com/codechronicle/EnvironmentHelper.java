package com.codechronicle;

public class EnvironmentHelper {
	
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

		System.setProperty("jdbcURL", databaseURL.replaceAll(
				"postgres://(.*):(.*)@(.*)",
				"jdbc:postgresql://$3?user=$1&password=$2"));
	}
}
