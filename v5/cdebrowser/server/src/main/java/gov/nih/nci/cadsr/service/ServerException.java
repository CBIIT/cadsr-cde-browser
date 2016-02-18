package gov.nih.nci.cadsr.service;

import org.springframework.http.HttpStatus;

public class ServerException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpStatus httpCode = HttpStatus.INTERNAL_SERVER_ERROR;

	public HttpStatus getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(HttpStatus httpCode) {
		this.httpCode = httpCode;
	}
	

	public ServerException() {
		super();
	}

	public ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(String message) {
		super(message);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return "ServerException [httpCode=" + httpCode + ", " + super.toString() + "]";
	}
	
}
