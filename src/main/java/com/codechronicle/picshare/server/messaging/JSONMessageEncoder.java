package com.codechronicle.picshare.server.messaging;

import org.codehaus.jackson.map.ObjectMapper;

public class JSONMessageEncoder implements MessageEncoder {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	/* (non-Javadoc)
	 * @see com.codechronicle.picshare.server.messaging.MessageEncoder#toEncodedString(java.lang.Object)
	 */
	@Override
	public String toEncodedString(Object obj) {
		
		String json = null;
		try {
			json = mapper.writeValueAsString(obj);
		} catch (Throwable e) {
			throw new RuntimeException("Unable to map to JSON", e);
		}
		
		return json;
	}
	
	/* (non-Javadoc)
	 * @see com.codechronicle.picshare.server.messaging.MessageEncoder#fromEncodedString(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T fromEncodedString(String encodedMsg, Class<T> clazz) {
		
		T obj  = null;
		
		try {
			obj = mapper.readValue(encodedMsg, clazz);
		} catch (Throwable e) {
			throw new RuntimeException("Unable to map from JSON : \n" + encodedMsg, e);
		}
		
		return obj;
	}
}
