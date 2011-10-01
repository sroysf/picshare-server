package com.codechronicle.storage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.codechronicle.EnvironmentHelper;

public class LocalDiskPersistentStoreProvider implements
		PersistentStoreProvider {

	private File storageDir = null;
	
	public LocalDiskPersistentStoreProvider(String directory) {
		super();
		
		this.storageDir = new File(directory);
	}
	
	/**
	 * Randomly distribute between many different buckets just so no single directory accumulates
	 * too many files. Too many files in a single directory can become difficult to manage.
	 * 
	 * @return
	 */
	private File selectDirectory() {
		int bucketNum = (int)(Math.random() * 100);
		File dir = new File(storageDir, "bucket"+bucketNum);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	@Override
	public URL persistFile(File srcFile) {
		
		File targetDir = selectDirectory();
		//File targetDir = storageDir;
		
		System.out.println("Saving file : " + srcFile.getAbsolutePath() + " to local disk in directory : " + targetDir.getAbsolutePath());
		
		try {
			FileUtils.copyFileToDirectory(srcFile, targetDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			URL url = new URL(EnvironmentHelper.getAppDataURLRoot() + targetDir.getName() + "/" + srcFile.getName());
			return url;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public File getDirectory() {
		return storageDir;
	}
}
