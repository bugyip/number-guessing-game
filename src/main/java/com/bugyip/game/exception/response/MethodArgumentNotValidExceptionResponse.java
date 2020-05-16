package com.bugyip.game.exception.response;

import java.util.Date;
import java.util.List;

public class MethodArgumentNotValidExceptionResponse {

	private Date timestamp;

	private List<String> messages;

	public MethodArgumentNotValidExceptionResponse(Date timestamp, List<String> messages) {
		this.timestamp = timestamp;
		this.messages = messages;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
