package com.unir.products.controller.model;

import com.unir.products.data.utils.Consts;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {
	
	private String titulo;
	
	private String autor;
	
	private LocalDate fechaDePublicacion;
	
	private String editorial;
	
	private String categoria;
	
	private String isbn;
	
	private String portada;
	
	private String sinopsis;
	
	private Double valoracion;
	
	private Boolean visible;
	
	private Integer stock;
	
	private Double precio;
}
