package com.unir.products.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

	
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
