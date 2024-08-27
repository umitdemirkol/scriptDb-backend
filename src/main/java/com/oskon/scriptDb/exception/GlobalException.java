package com.oskon.scriptDb.exception;

import org.springframework.http.HttpStatus;

public class GlobalException extends Exception {

	private static final long serialVersionUID = 1L;

	private Throwable originalException;
	private Long code;
	private String message;
	private HttpStatus status;

	public GlobalException(Long code, String message, HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}

	public Throwable getOriginalException() {
		return originalException;
	}

	public void setOriginalException(Throwable originalException) {
		this.originalException = originalException;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
