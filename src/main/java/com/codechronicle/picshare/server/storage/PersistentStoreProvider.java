package com.codechronicle.picshare.server.storage;

import java.io.File;
import java.net.URL;

public interface PersistentStoreProvider {

	URL persistFile(File srcFile);
}
