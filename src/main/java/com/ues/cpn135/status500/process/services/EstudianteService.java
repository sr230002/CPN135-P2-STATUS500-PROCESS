package com.ues.cpn135.status500.process.services;

import com.ues.cpn135.status500.process.dtos.EstudianteRequest;
import com.ues.cpn135.status500.process.entities.Estudiante;

public interface EstudianteService {

	Estudiante guardar(EstudianteRequest req);
}
