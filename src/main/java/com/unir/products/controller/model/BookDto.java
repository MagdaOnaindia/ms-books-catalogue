package com.unir.products.controller.model;

import com.unir.products.data.utils.Consts;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {
	
	private String titulo;
	
	private String autor;
	
	private String fechaDePublicacion;
	
	private String editorial;
	
	private String categoria;
	
	private Long isbn;
	
	private String portada;
	
	private String sinopsis;
	
	private Double valoracion;
	
	private Boolean visible;
	
	private Boolean stock;
	
	private Double precio;
}
