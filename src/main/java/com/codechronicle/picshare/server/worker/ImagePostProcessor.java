package com.codechronicle.picshare.server.worker;

import org.springframework.transaction.annotation.Transactional;

/**
 * Transaction annotations in Spring ONLY work when invoked through a dynamic proxy interface. That's the
 * main reason for this interface to exist. WARNING : Transaction annotations won't work even within an
 * instantiated class, if the class calls another private method (for example). The transactions only
 * work when called through the interface. Sometimes I really wonder if all this Spring magic is worth
 * it.
 * 
 * @author sroy
 *
 */
public interface ImagePostProcessor {

	void processImage(long imageId, boolean hostOriginal);

}