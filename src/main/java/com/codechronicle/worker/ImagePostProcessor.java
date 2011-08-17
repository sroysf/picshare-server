package com.codechronicle.worker;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.codechronicle.EnvironmentHelper;
import com.codechronicle.dao.ImageDAO;
import com.codechronicle.entity.Image;
import com.codechronicle.messaging.MessageQueue;
import com.codechronicle.messaging.ProcessImageMessage;
import com.codechronicle.storage.PersistentStoreProvider;

@Service
public class ImagePostProcessor {
	
	private static ApplicationContext context = null;
	private static Logger log = LoggerFactory.getLogger(ImagePostProcessor.class);

	@Inject
	private ImageDAO imageDAO;
	
	@Inject
	MessageQueue messageQueue;
	
	public static void main(String[] args) {

		EnvironmentHelper.configureEnvironment();

		context = new ClassPathXmlApplicationContext("appContext.xml");
		
		ImagePostProcessor imgProcessor = context.getBean("imagePostProcessor", ImagePostProcessor.class);
		imgProcessor.listenForMessages();
		
		/*Image image = new Image();
		image.setOriginalUrl("http://www.citypictures.net/data/media/227/Monch_and_Eiger_Grosse_Scheidegg_Switzerland.jpg");
		ImageDAO imgDAO = context.getBean("imageDAO", ImageDAO.class);
		imgDAO.saveOrUpdate(image);
		
		*/
		
		//imgProcessor.processImage(2l);
		
	}
	
	private void listenForMessages() {
		int i=0;
		while (true) {
			i++;
			ProcessImageMessage msg = messageQueue.dequeue(EnvironmentHelper.PROCESS_IMAGE_QUEUE, ProcessImageMessage.class);
			System.out.println("Got message #" + i + ", image id = " + msg.getImageId());
			processImage(msg.getImageId(), msg.isHostOriginal());
		}
	}
	
	private void processImage(long imageId, boolean hostOriginal) {

		Image image = imageDAO.findById(imageId);
		
		if (image == null) {
			throw new RuntimeException("Unable to locate image with id = " + imageId + " in database.");
		}
		
		if (image.isPostProcessed()) {
			log.warn("Request to process image id = " + imageId + ", but image has already post processed. Update database status to force reprocessing");
			return;
		}
		
		String url = image.getOriginalUrl();
		
		File srcImageFile = copyImageFromURL(url);
		File webImageFile = createWebImage(srcImageFile);
		File thumbImageFile = createThumbImage(srcImageFile);
		
		try {
			PersistentStoreProvider storeProvider = context.getBean("storageProvider", PersistentStoreProvider.class);
			URL webImageURL = storeProvider.persistFile(webImageFile);
			URL thumbImageURL = storeProvider.persistFile(thumbImageFile);
			String masterImageURL = null;
			
			if (hostOriginal) {
				File masterImageFile = createMasterImage(srcImageFile);
				masterImageURL = storeProvider.persistFile(masterImageFile).toString();
			} else {
				masterImageURL = image.getOriginalUrl();
			}
			
			// TODO: Wire up actual database updates that update record with web and thumb URL's
			image.setWebUrl(webImageURL.toString());
			image.setThumbUrl(thumbImageURL.toString());
			image.setMasterUrl(masterImageURL);
			image.setPostProcessed(true);
			imageDAO.saveOrUpdate(image);
			
			System.out.println("Web image = " + webImageURL);
			System.out.println("Thumb image = " + thumbImageURL);
			
		} finally {
			srcImageFile.delete();
			webImageFile.delete();
			thumbImageFile.delete();
		}
	}

	private File createMasterImage(File srcImage) {
		File targetFile = new File(srcImage.getAbsolutePath() + ".master.jpg");
		System.out.println("Creating master file : " + targetFile.getAbsolutePath());
		convertImage(srcImage, targetFile);
		
		return targetFile;
	}
	
	private File createWebImage(File srcImage) {
		File targetFile = new File(srcImage.getAbsolutePath() + ".web.jpg");
		System.out.println("Creating web file : " + targetFile.getAbsolutePath());
		resizeImage(srcImage, targetFile, "1024x768");
		
		return targetFile;
	}
	
	private File createThumbImage(File srcImage) {
		File targetFile = new File(srcImage.getAbsolutePath() + ".thumb.jpg");
		System.out.println("Creating web file : " + targetFile.getAbsolutePath());
		resizeImage(srcImage, targetFile, "200x200");

		return targetFile;
	}


	private void convertImage(File srcImage, File targetFile) {
		String cmdLine = "/usr/bin/convert " + srcImage.getAbsolutePath() + " " + targetFile.getAbsolutePath();
		executeCommandLine(targetFile, cmdLine);
	}
	
	private void resizeImage(File srcImage, File targetFile, String dimensions) {
		String cmdLine = "/usr/bin/convert " + srcImage.getAbsolutePath() + " -resize " + dimensions + " " + targetFile.getAbsolutePath();
		executeCommandLine(targetFile, cmdLine);
	}

	private void executeCommandLine(File targetFile, String cmdLine) {
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


	private File copyImageFromURL(String url) {
		
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
