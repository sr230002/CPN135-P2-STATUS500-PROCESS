package com.ues.cpn135.status500.process.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.ues.cpn135.status500.process.dtos.EstudianteRequest;
import com.ues.cpn135.status500.process.entities.Estudiante;
import com.ues.cpn135.status500.process.exceptions.ApiException;
import com.ues.cpn135.status500.process.repositories.EstudianteRepository;

import tools.jackson.databind.ObjectMapper;

class EstudianteServicesImplTest {

	private static final EstudianteRequest REQUEST = new EstudianteRequest("Ana Maria", "Lopez Perez",
			LocalDate.of(2003, 5, 14), new BigDecimal("1.75"), new BigDecimal("70"), "GT01", "LP12345");

	private final EstudianteRepository repository = mock(EstudianteRepository.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void calculaElImcConElPesoDelRequest() {
		when(repository.save(any(Estudiante.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

		Estudiante guardado = servicio().guardar(REQUEST);

		// 70 / 1.75^2 = 22.8571... -> 22.86
		assertEquals(new BigDecimal("22.86"), guardado.getImc());
		assertEquals(new BigDecimal("70"), guardado.getPesoKg());
		assertEquals(new BigDecimal("1.75"), guardado.getEstaturaM());
		assertEquals("LP12345", guardado.getCarnet());
	}

	@Test
	void elImcCambiaConElPesoRecibido() {
		when(repository.save(any(Estudiante.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

		EstudianteRequest liviano = new EstudianteRequest("Ana Maria", "Lopez Perez", LocalDate.of(2003, 5, 14),
				new BigDecimal("1.75"), new BigDecimal("50"), "GT01", "LP12345");

		// 50 / 1.75^2 = 16.3265... -> 16.33
		assertEquals(new BigDecimal("16.33"), servicio().guardar(liviano).getImc());
	}

	@Test
	void rechazaUnCarnetYaProcesado() {
		when(repository.existsByCarnet("LP12345")).thenReturn(true);

		ApiException excepcion = assertThrows(ApiException.class, () -> servicio().guardar(REQUEST));

		assertEquals(HttpStatus.CONFLICT, excepcion.getStatus());
	}

	@Test
	void rechazaUnImcQueNoCabeEnLaColumna() {
		EstudianteRequest bajito = new EstudianteRequest("Beto", "Corto", LocalDate.of(2004, 1, 1),
				new BigDecimal("0.50"), new BigDecimal("70"), "GT03", "CC11111");

		ApiException excepcion = assertThrows(ApiException.class, () -> servicio().guardar(bajito));

		assertEquals(HttpStatus.BAD_REQUEST, excepcion.getStatus());
	}

	private EstudianteServicesImpl servicio() {
		return new EstudianteServicesImpl(repository, objectMapper);
	}
}
