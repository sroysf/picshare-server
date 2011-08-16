package com.codechronicle.worker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.codechronicle.EnvironmentHelper;
import com.codechronicle.messaging.AsyncMessage;
import com.codechronicle.messaging.MessageQueue;

public class ImagePostProcessor {

	public static void main(String[] args) {

		EnvironmentHelper.configureEnvironment();

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("appContext.xml");

		MessageQueue queue = ctx.getBean("messageQueue", MessageQueue.class);

		// TODO: Wire up real image requests that call processImage()
		// TODO: Add option in message to specify a local source file. 
		//		 Must include hosting URL to point at original. This very simply delegates the security issue to the browser.
		
		int i=0;
		while (true) {
			i++;
			AsyncMessage msg = queue.dequeue("testQueue", AsyncMessage.class);
			System.out.println("Got message #" + i + " -> " + msg);
		}
	}
	
	public static void example() {
		processImage("https://picasaweb.google.com/102738096689107661272/AustriaParagliding#5627286372394737378");
	}
	
	private static void processImage(String url) {

		File srcImageFile = copyImageFromURL(url);
		createWebImage(srcImageFile);
		createThumbImage(srcImageFile);
		
		// TODO: Push the derived artifacts to permanent storage (like S3)
		// TODO: Wire up actual database updates that update record with web and thumb URL's
		// TODO: Delete temp image files
	}


	private static File createWebImage(File srcImage) {
		File targetFile = new File(srcImage.getAbsolutePath() + ".web.jpg");
		System.out.println("Creating web file : " + targetFile.getAbsolutePath());
		resizeImage(srcImage, targetFile, "1024x768");
		
		return targetFile;
	}
	
	private static File createThumbImage(File srcImage) {
		File targetFile = new File(srcImage.getAbsolutePath() + ".thumb.jpg");
		System.out.println("Creating web file : " + targetFile.getAbsolutePath());
		resizeImage(srcImage, targetFile, "200x200");

		return targetFile;
	}


	private static void resizeImage(File srcImage, File targetFile, String dimensions) {
		
		String cmdLine = "/usr/bin/convert " + srcImage.getAbsolutePath() + " -resize " + dimensions + " " + targetFile.getAbsolutePath();
		try {
			Process proc = Runtime.getRuntime().exec(cmdLine);
			int exitCode = proc.waitFor();
			
			if (exitCode == 0) {
				// Seems to have worked. Verify target file exists.
				if (targetFile.exists() && (targetFile.length() > 0)) {
					return;
				} else {
					throw new Exception("Image resize failed unexpectedly. Command line = " + cmdLine);
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}


	private static File copyImageFromURL(String url) {
		
		File tmpFile = null;
		try {
			URI uri = new URI(url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(uri);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				do {
					tmpFile = File.createTempFile("picshare", "");
				} while (!tmpFile.exists());
			    InputStream instream = entity.getContent();
			    FileUtils.copyInputStreamToFile(instream, tmpFile);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		
		return tmpFile;
	}
}
