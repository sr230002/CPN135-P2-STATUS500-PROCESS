package com.ues.cpn135.status500.process.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estudiante")
public class Estudiante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 100)
	private String nombres;

	@Column(nullable = false, length = 100)
	private String apellidos;

	@Column(name = "estatura_m", nullable = false, precision = 5, scale = 2)
	private BigDecimal estaturaM;

	@Column(name = "peso_kg", nullable = false, precision = 5, scale = 2)
	private BigDecimal pesoKg;

	@Column(nullable = false, precision = 4, scale = 2)
	private BigDecimal imc;

	@Column(name = "grupo_teorico", nullable = false, length = 4)
	private String grupoTeorico;

	@Column(nullable = false, length = 7, unique = true)
	private String carnet;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;
}
