package com.codechronicle.messaging;


public class ProcessImageMessage extends AsyncMessage {
	
	private long imageId;
	
	public ProcessImageMessage() {
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

}
