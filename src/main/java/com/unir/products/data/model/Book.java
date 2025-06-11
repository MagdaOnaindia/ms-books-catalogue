package com.unir.products.data.model;

import com.unir.products.controller.model.BookDto;
import com.unir.products.data.utils.Consts;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "libros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = Consts.TITULO, unique = true)
	private String titulo;
	
	@Column(name = Consts.AUTOR)
	private String autor;
	
	@Column(name = Consts.FECHA_DE_PUBLICACION)
	/* private LocalDate fechaDePublicacion; */
	private LocalDate fecha_de_publicacion;

	@Column(name = Consts.EDITORIAL)
	private String editorial;
	
	@Column(name = Consts.CATEGORIA)
	private String categoria;
	
	@Column(name = Consts.ISBN)
	private String isbn;
	
	@Column(name = Consts.PORTADA)
	private String portada;
	
	@Column(name = Consts.SINOPSIS)
	private String sinopsis;
	
	@Column(name = Consts.VALORACION)
	private Double valoracion;
	
	@Column(name = Consts.VISIBLE)
	private Boolean visible;
	
	@Column(name = Consts.STOCK)
	private Integer stock;
	
	@Column(name = Consts.PRECIO)
	private Double precio;
	

	public void update(BookDto bookDto) {
		
		this.titulo = bookDto.getTitulo();
		this.autor = bookDto.getAutor();
		/* this.fechaDePublicacion = bookDto.getFechaDePublicacion(); */
		this.fecha_de_publicacion = bookDto.getFecha_de_publicacion();
		this.editorial = bookDto.getEditorial();
		this.categoria = bookDto.getCategoria();
		this.isbn = bookDto.getIsbn();
		this.portada = bookDto.getPortada();
		this.sinopsis = bookDto.getSinopsis();
		this.valoracion = bookDto.getValoracion();
		this.visible = bookDto.getVisible();
		this.stock = bookDto.getStock();
		this.precio = bookDto.getPrecio();
	}

}
