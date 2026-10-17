package com.ues.cpn135.status500.process.exceptions;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.ues.cpn135.status500.process.dtos.ApiResponse;
import com.ues.cpn135.status500.process.utilities.HandlerResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<Object>> handleApi(ApiException e) {
		log.debug("Excepción de API ({}): {}", e.getStatus(), e.getMessage());
		return HandlerResponse.error(e.getStatus(), msj(e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException e) {
		String msj = e.getBindingResult().getFieldErrors().stream()
				.sorted(Comparator.comparing(FieldError::getField, Comparator.nullsLast(Comparator.naturalOrder())))
				.map(err -> err.getField() + ": " + err.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return HandlerResponse.error(HttpStatus.BAD_REQUEST,
				msj.isBlank() ? "Los datos enviados no son válidos." : msj);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Object>> handleNotReadable(HttpMessageNotReadableException e) {
		log.debug("Cuerpo de petición no legible", e);
		return HandlerResponse.error(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es un JSON válido.");
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleNoRoute(NoResourceFoundException e) {
		return HandlerResponse.error(HttpStatus.NOT_FOUND, "Ruta no encontrada.");
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<Object>> handleDataIntegrity(DataIntegrityViolationException e) {
		log.debug("Violación de integridad de datos", e);
		return HandlerResponse.error(HttpStatus.CONFLICT,
				"El recurso ya existe o viola una restricción de la base de datos.");
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
		log.debug("Método no soportado: {}", e.getMethod());
		return HandlerResponse.error(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP no soportado para esta ruta.");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		log.debug("Tipo de argumento inválido en '{}'", e.getName());
		return HandlerResponse.error(HttpStatus.BAD_REQUEST,
				"El parámetro '" + e.getName() + "' tiene un formato inválido.");
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ApiResponse<Object>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
		log.debug("Content-Type no soportado: {}", e.getContentType());
		return HandlerResponse.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
				"Tipo de contenido no soportado. Usa application/json.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleUnexpected(Exception e) {
		log.error("Error no controlado", e);
		return HandlerResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
				"Ocurrió un error inesperado en el servidor.");
	}

	private static String msj(String message) {
		return message == null || message.isBlank()
				? "Ocurrió un error inesperado en el servidor."
				: message;
	}
}
