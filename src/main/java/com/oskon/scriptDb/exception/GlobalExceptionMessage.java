package com.oskon.scriptDb.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class GlobalExceptionMessage {

	private HttpStatus status;
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private Long code;
	private String path;

	public GlobalExceptionMessage() {
		timestamp = LocalDateTime.now();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
