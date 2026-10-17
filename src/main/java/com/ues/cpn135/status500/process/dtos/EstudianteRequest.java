package com.ues.cpn135.status500.process.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteRequest {

	@NotBlank
	@Size(max = 100)
	private String nombres;

	@NotBlank
	@Size(max = 100)
	private String apellidos;

	@NotNull
	@PastOrPresent
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaNacimiento;

	@NotNull
	@DecimalMin(value = "0.0", inclusive = false)
	@Digits(integer = 3, fraction = 2)
	private BigDecimal estaturaM;

	@NotNull
	@DecimalMin(value = "0.0", inclusive = false)
	@Digits(integer = 3, fraction = 2)
	private BigDecimal pesoKG;

	@NotBlank
	@Pattern(regexp = "^GT[0-9]{2}$")
	private String grupoTeorico;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{2}[0-9]{5}$")
	private String carnet;
}
