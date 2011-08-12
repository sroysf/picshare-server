package com.codechronicle.messaging;

public interface MessageEncoder {

	String toEncodedString(Object obj);

	<T> T fromEncodedString(String encodedMsg, Class<T> clazz);

}