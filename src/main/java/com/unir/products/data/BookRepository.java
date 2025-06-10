package com.unir.products.data;

import com.unir.products.controller.model.BookSearchCriteria;
import com.unir.products.data.utils.Consts;
import com.unir.products.data.utils.SearchCriteria;
import com.unir.products.data.utils.SearchOperation;
import com.unir.products.data.utils.SearchStatement;
import com.unir.products.data.model.Book;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final BookJpaRepository repository;

    public List<Book> getBooks() {
        return repository.findAll();
    }

    public Book getById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public Book getByIsbn(Long isbn) {
        return repository.findByIsbn(isbn);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public List<Book> search(BookSearchCriteria criteria) {
        SearchCriteria<Book> spec = new SearchCriteria<>();

        if (StringUtils.isNotBlank(criteria.getTitulo())) {
            spec.add(new SearchStatement(Consts.TITULO, criteria.getTitulo(), SearchOperation.MATCH));
        }
        if (StringUtils.isNotBlank(criteria.getAutor())) {
            spec.add(new SearchStatement(Consts.AUTOR, criteria.getAutor(), SearchOperation.MATCH));
        }
        if (criteria.getFechaDePublicacionDesde() != null) {
            spec.add(new SearchStatement(Consts.FECHA_DE_PUBLICACION, criteria.getFechaDePublicacionDesde(), SearchOperation.GREATER_THAN_EQUAL));
        }
        if (criteria.getFechaDePublicacionHasta() != null) {
            spec.add(new SearchStatement(Consts.FECHA_DE_PUBLICACION, criteria.getFechaDePublicacionHasta(), SearchOperation.LESS_THAN_EQUAL));
        }
        if (StringUtils.isNotBlank(criteria.getEditorial())) {
            spec.add(new SearchStatement(Consts.EDITORIAL, criteria.getEditorial(), SearchOperation.MATCH));
        }
        if (StringUtils.isNotBlank(criteria.getCategoria())) {
            spec.add(new SearchStatement(Consts.CATEGORIA, criteria.getCategoria(), SearchOperation.EQUAL));
        }
        if (StringUtils.isNotBlank(criteria.getIsbn())) {
            spec.add(new SearchStatement(Consts.ISBN, criteria.getIsbn(), SearchOperation.EQUAL)); //también se pueden permitir búsquedas parciales con MATCH
        }
        if (criteria.getValoracionMin() != null) {
            spec.add(new SearchStatement(Consts.VALORACION, criteria.getValoracionMin(), SearchOperation.GREATER_THAN_EQUAL));
        }
        if (criteria.getVisible() != null) {
            spec.add(new SearchStatement(Consts.VISIBLE, criteria.getVisible(), SearchOperation.EQUAL));
        }
        if (criteria.getConStock() != null && criteria.getConStock()) {
            //libros con stock > 0
            spec.add(new SearchStatement(Consts.STOCK, 0, SearchOperation.GREATER_THAN));
        }
        if (criteria.getPrecioMin() != null) {
            spec.add(new SearchStatement(Consts.PRECIO, criteria.getPrecioMin(), SearchOperation.GREATER_THAN_EQUAL));
        }
        if (criteria.getPrecioMax() != null) {
            spec.add(new SearchStatement(Consts.PRECIO, criteria.getPrecioMax(), SearchOperation.LESS_THAN_EQUAL));
        }

        return repository.findAll(spec);
    }

}
