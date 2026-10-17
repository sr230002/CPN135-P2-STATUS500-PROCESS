package com.ues.cpn135.status500.process.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

	private final HttpStatus status;

	public ApiException(String msj) {
		this(HttpStatus.BAD_REQUEST, msj);
	}

	public ApiException(HttpStatus status, String msj) {
		super(msj);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
