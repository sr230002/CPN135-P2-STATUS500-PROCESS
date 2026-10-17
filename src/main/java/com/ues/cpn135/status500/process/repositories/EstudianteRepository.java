package com.ues.cpn135.status500.process.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.cpn135.status500.process.entities.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

	boolean existsByCarnet(String carnet);
}
