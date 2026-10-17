package com.ues.cpn135.status500.process.dtos;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	private T data;
	private String message;
	private String method;

	public static <T> ApiResponse<T> of(T data, String message) {
		return new ApiResponse<>(data, message, currentMethod());
	}

	public static String currentMethod() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs != null ? attrs.getRequest().getMethod() : "UNKNOWN";
	}
}
