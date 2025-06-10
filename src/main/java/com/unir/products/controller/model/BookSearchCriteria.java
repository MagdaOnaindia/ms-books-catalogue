package com.unir.products.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchCriteria {
    private String titulo;
    private String autor;
    private LocalDate fechaDePublicacionDesde;
    private LocalDate fechaDePublicacionHasta;
    private String editorial;
    private String categoria;
    private String isbn;
    private Double valoracionMin;
    private Boolean visible;
    private Boolean conStock;
    private Double precioMin;
    private Double precioMax;
}