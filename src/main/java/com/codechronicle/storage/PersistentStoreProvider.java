package com.codechronicle.storage;

import java.io.File;
import java.net.URL;

public interface PersistentStoreProvider {

	URL persistFile(File srcFile);
}
