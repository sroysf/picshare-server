package com.codechronicle.picshare.server.messaging;

public interface MessageEncoder {

	String toEncodedString(Object obj);

	<T> T fromEncodedString(String encodedMsg, Class<T> clazz);

}