package com.unir.products.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {


	@NotBlank(message = "El título no puede estar vacío")
	@Size(max = 255, message = "El título no puede exceder los 255 caracteres")
	private String titulo;

	@NotBlank(message = "El autor no puede estar vacío")
	@Size(max = 100, message = "El autor no puede exceder los 100 caracteres")
	private String autor;

	@NotNull(message = "La fecha de publicación es obligatoria")
	private LocalDate fechaDePublicacion;

	@NotBlank(message = "La editorial no puede estar vacía")
	@Size(max = 100, message = "La editorial no puede exceder los 100 caracteres")
	private String editorial;

	@NotBlank(message = "La categoría no puede estar vacía")
	@Size(max = 50, message = "La categoría no puede exceder los 50 caracteres")
	private String categoria;

	@NotBlank(message = "El ISBN no puede estar vacío")
	@Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", message = "ISBN no válido")
	private String isbn;

	@NotBlank(message = "La URL de la portada es obligatoria")
	@Size(max = 2048, message = "La URL de la portada es demasiado larga")
	private String portada;

	@Size(max = 2000, message = "La sinopsis no puede exceder los 2000 caracteres")
	private String sinopsis;

	@Min(value = 0, message = "La valoración no puede ser negativa")
	@Max(value = 5, message = "La valoración no puede ser mayor a 5")
	private Double valoracion;

	@NotNull(message = "El estado de visibilidad es obligatorio")
	private Boolean visible;

	@NotNull(message = "El stock es obligatorio")
	@Min(value = 0, message = "El stock no puede ser negativo")
	private Integer stock;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que cero")
	private Double precio;
}
