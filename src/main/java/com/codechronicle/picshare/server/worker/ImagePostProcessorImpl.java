package com.codechronicle.picshare.server.worker;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codechronicle.HttpConnectionHelper;
import com.codechronicle.picshare.server.EnvironmentHelper;
import com.codechronicle.picshare.server.entity.Image;
import com.codechronicle.picshare.server.storage.PersistentStoreProvider;

@Service
public class ImagePostProcessorImpl implements ImagePostProcessor {
	
	private static Logger log = LoggerFactory.getLogger(ImagePostProcessorImpl.class);
	
	private DefaultHttpClient client = new DefaultHttpClient();

	@Inject
	PersistentStoreProvider storageProvider;
	
	@PersistenceContext
	EntityManager em;
	

	public ImagePostProcessorImpl() {
		
		// Configure http client
		HttpConnectionHelper.configureSecurityFromPropertyFile(client, "server.props");
		HttpConnectionHelper.setNoSSLCertVerification(client);
	}
	
	@Override
	@Transactional
	public void processImage(long imageId, boolean hostOriginal) {

		log.debug("Loading and processing image id = " + imageId);
		
		Image image = em.find(Image.class, imageId);
		
		if (image == null) {
			throw new RuntimeException("Unable to locate image with id = " + imageId + " in database.");
		}
		
		if (image.isPostProcessed()) {
			log.warn("Request to process image id = " + imageId + ", but image has already post processed. Update database status to force reprocessing");
			return;
		}
		
		String url = image.getOriginalUrl();
		
		File srcImageFile = null;
		
		srcImageFile = copyImageFromURL(url);
		
		File webImageFile = createWebImage(srcImageFile);
		File thumbImageFile = createThumbImage(srcImageFile);
		
		try {
			URL webImageURL = storageProvider.persistFile(webImageFile);
			URL thumbImageURL = storageProvider.persistFile(thumbImageFile);
			String masterImageURL = null;
			
			if (hostOriginal) {
				File masterImageFile = createMasterImage(srcImageFile);
				masterImageURL = storageProvider.persistFile(masterImageFile).toString();
			} else {
				masterImageURL = image.getOriginalUrl();
			}
			
			// TODO: Wire up actual database updates that update record with web and thumb URL's
			image.setWebUrl(webImageURL.toString());
			image.setThumbUrl(thumbImageURL.toString());
			image.setMasterUrl(masterImageURL);
			image.setPostProcessed(true);
			
			log.info("=====> Master image = " + masterImageURL);
			log.info("=====> Web image = " + webImageURL);
			log.info("=====> Thumb image = " + thumbImageURL);
		} catch (Exception ex) {
			log.warn("Unexpected exception", ex);
		} finally {
			srcImageFile.delete();
			webImageFile.delete();
			thumbImageFile.delete();
		}
	}

	private File createMasterImage(File srcImage) {
		File targetFile = new File(srcImage.getAbsolutePath() + ".master.jpg");
		log.info("Creating master file : " + targetFile.getAbsolutePath());
		convertImage(srcImage, targetFile);
		
		return targetFile;
	}
	
	private File createWebImage(File srcImage) {
		File targetFile = new File(srcImage.getAbsolutePath() + ".web.jpg");
		log.info("Creating web file : " + targetFile.getAbsolutePath());
		resizeImage(srcImage, targetFile, "1024x768");
		
		return targetFile;
	}
	
	private File createThumbImage(File srcImage) {
		File targetFile = new File(srcImage.getAbsolutePath() + ".thumb.jpg");
		log.info("Creating web file : " + targetFile.getAbsolutePath());
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
			} else {
				throw new RuntimeException("Image resize failed unexpectedly. Command line = " + cmdLine);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private File copyImageFromURL(String url) {
		
		File tmpFile = null;
		try {
			URI uri = new URI(url);
			
			HttpGet httpget = new HttpGet(uri);
			HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				do {
					tmpFile = File.createTempFile(EnvironmentHelper.getAppDataDirName(), "");
				} while (!tmpFile.exists());
			    InputStream instream = entity.getContent();
			    
			    log.info("Downloading file from " + url + " --> " + tmpFile.getAbsolutePath());
			    FileUtils.copyInputStreamToFile(instream, tmpFile);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		
		return tmpFile;
	}
}
