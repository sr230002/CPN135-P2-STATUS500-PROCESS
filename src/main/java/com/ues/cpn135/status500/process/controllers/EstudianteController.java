package com.ues.cpn135.status500.process.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.cpn135.status500.process.dtos.ApiResponse;
import com.ues.cpn135.status500.process.dtos.EstudianteRequest;
import com.ues.cpn135.status500.process.entities.Estudiante;
import com.ues.cpn135.status500.process.services.EstudianteService;
import com.ues.cpn135.status500.process.utilities.HandlerResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/almacenar")
@RequiredArgsConstructor
public class EstudianteController {

	private final EstudianteService service;

	@PostMapping
	public ResponseEntity<ApiResponse<Estudiante>> guardar(@Valid @RequestBody EstudianteRequest req) {
		Estudiante entidad = service.guardar(req);
		return HandlerResponse.created(entidad, "Datos procesados y almacenados correctamente en status500process.");
	}
}
