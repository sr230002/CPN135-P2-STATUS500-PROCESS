package com.ues.cpn135.status500.process.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ues.cpn135.status500.process.dtos.ApiResponse;

public final class HandlerResponse {

	private HandlerResponse() {
	}

	public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
		return ok(data, "Operación realizada con éxito");
	}

	public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
		return build(HttpStatus.OK, data, message);
	}

	public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
		return build(HttpStatus.CREATED, data, "Creado");
	}

	public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
		return build(HttpStatus.CREATED, data, message);
	}

	public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
		return build(status, null, message);
	}

	private static <T> ResponseEntity<ApiResponse<T>> build(HttpStatus status, T data, String message) {
		return ResponseEntity.status(status)
				.body(new ApiResponse<>(data, message, ApiResponse.currentMethod()));
	}
}
