package com.codechronicle.picshare.server.messaging;

import java.io.Serializable;
import java.util.Date;

public class AsyncMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date timeStamp;
	
	public AsyncMessage() {
		timeStamp = new Date();
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}
