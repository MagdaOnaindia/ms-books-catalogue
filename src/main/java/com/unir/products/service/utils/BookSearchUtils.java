package com.unir.products.service.utils;

import com.unir.products.controller.model.BookSearchCriteria;
import org.springframework.util.StringUtils;

public class BookSearchUtils {

    private BookSearchUtils() {

    }

    public static boolean criteriaAreNotEmpty(BookSearchCriteria criteria) {
        if (criteria == null) return false;
        return StringUtils.hasText(criteria.getTitulo())
                || StringUtils.hasText(criteria.getAutor())
                || criteria.getFechaDePublicacionDesde() != null
                || criteria.getFechaDePublicacionHasta() != null
                || StringUtils.hasText(criteria.getEditorial())
                || StringUtils.hasText(criteria.getCategoria())
                || StringUtils.hasText(criteria.getIsbn())
                || criteria.getValoracionMin() != null
                || criteria.getVisible() != null
                || (criteria.getConStock() != null && criteria.getConStock())
                || criteria.getPrecioMin() != null
                || criteria.getPrecioMax() != null;
    }
}