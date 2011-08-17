package com.codechronicle.messaging;


public class ProcessImageMessage extends AsyncMessage {
	
	private long imageId;
	private boolean hostOriginal = false;
	
	public ProcessImageMessage() {
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public boolean isHostOriginal() {
		return hostOriginal;
	}

	public void setHostOriginal(boolean hostOriginal) {
		this.hostOriginal = hostOriginal;
	}
}
