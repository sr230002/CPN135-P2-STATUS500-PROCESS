package com.ues.cpn135.status500.process.implementations;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ues.cpn135.status500.process.dtos.EstudianteRequest;
import com.ues.cpn135.status500.process.entities.Estudiante;
import com.ues.cpn135.status500.process.exceptions.ApiException;
import com.ues.cpn135.status500.process.repositories.EstudianteRepository;
import com.ues.cpn135.status500.process.services.EstudianteService;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class EstudianteServicesImpl implements EstudianteService {

	private static final BigDecimal IMC_MAXIMO = new BigDecimal("99.99");

	private final EstudianteRepository repository;
	private final ObjectMapper objectMapper;

	public EstudianteServicesImpl(EstudianteRepository repository, ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	@Override
	@Transactional
	public Estudiante guardar(EstudianteRequest req) {
		// Requisito del examen: el JSON recibido debe verse con `docker logs -f`.
		log.info("JSON recibido en /almacenar:\n{}", jsonPretty(req));

		BigDecimal estatura = req.getEstaturaM();
		BigDecimal peso = req.getPesoKG();
		BigDecimal imc = peso.divide(estatura.pow(2), 2, RoundingMode.HALF_UP);
		log.info("Calculo IMC -> pesoKG={}, estaturaM={}, imc={}", peso, estatura, imc);

		// La columna es numeric(4,2): un IMC mayor a 99.99 desbordaría la base.
		if (imc.compareTo(IMC_MAXIMO) > 0) {
			throw new ApiException(HttpStatus.BAD_REQUEST,
					"La estatura " + estatura + " m produce un IMC de " + imc + ", fuera del rango permitido (máx. "
							+ IMC_MAXIMO + ").");
		}

		if (repository.existsByCarnet(req.getCarnet())) {
			throw new ApiException(HttpStatus.CONFLICT, "Ya existe un estudiante procesado con ese carnet.");
		}

		Estudiante estudiante = Estudiante.builder()
				.nombres(req.getNombres())
				.apellidos(req.getApellidos())
				.estaturaM(estatura)
				.pesoKg(peso)
				.imc(imc)
				.grupoTeorico(req.getGrupoTeorico())
				.carnet(req.getCarnet())
				.build();

		return repository.save(estudiante);
	}

	/** El JSON tal cual llegó, con saltos de línea para que se lea en `docker logs -f`. */
	private String jsonPretty(EstudianteRequest req) {
		try {
			return objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(req);
		} catch (JacksonException e) {
			log.warn("No se pudo formatear el JSON recibido: {}", e.getMessage());
			return String.valueOf(req);
		}
	}
}
