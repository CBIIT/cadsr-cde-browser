package gov.nih.nci.cadsr.service;

import org.springframework.http.HttpStatus;

public class ClientException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpStatus httpCode = HttpStatus.BAD_REQUEST;

	public HttpStatus getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(HttpStatus httpCode) {
		this.httpCode = httpCode;
	}
	
	public ClientException() {
		super();
	}

	public ClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientException(String message) {
		super(message);
	}

	public ClientException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return "ClientException [httpCode=" + httpCode + ", " + super.toString() + "]";
	}
	
}
