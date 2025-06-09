package com.unir.products.data;

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

    public List<Book> search(String titulo, String autor, String fechaDePublicacion, String editorial, String categoria, Long isbn, String sinopsis, Double valoracion, Boolean visible, Boolean stock, Double precio) {
        SearchCriteria<Book> spec = new SearchCriteria<>();

        if (StringUtils.isNotBlank(titulo)) {
            spec.add(new SearchStatement(Consts.TITULO, titulo, SearchOperation.MATCH));
        }
        
        if (StringUtils.isNotBlank(autor)) {
            spec.add(new SearchStatement(Consts.AUTOR, autor, SearchOperation.MATCH));
        }
        
        if (StringUtils.isNotBlank(fechaDePublicacion)) {
            spec.add(new SearchStatement(Consts.FECHA_DE_PUBLICACION, fechaDePublicacion, SearchOperation.MATCH));
        }
        
        if (StringUtils.isNotBlank(editorial)) {
            spec.add(new SearchStatement(Consts.EDITORIAL, editorial, SearchOperation.MATCH));
        }
        
        if (StringUtils.isNotBlank(categoria)) {
            spec.add(new SearchStatement(Consts.CATEGORIA,  categoria, SearchOperation.EQUAL));
        }

        if (isbn != null) {
            spec.add(new SearchStatement(Consts.ISBN, isbn, SearchOperation.MATCH));
        }
        
        if (StringUtils.isNotBlank(sinopsis)) {
            spec.add(new SearchStatement(Consts.SINOPSIS,  sinopsis, SearchOperation.MATCH));
        }
        
        if (valoracion != null) {
            spec.add(new SearchStatement(Consts.VALORACION,  valoracion, SearchOperation.GREATER_THAN_EQUAL));
        }

        if (visible != null) {
            spec.add(new SearchStatement(Consts.VISIBLE, visible, SearchOperation.EQUAL));
        }
        
        if (stock != null) {
            spec.add(new SearchStatement(Consts.STOCK, stock, SearchOperation.EQUAL));
        }
        
        if (precio != null) {
            spec.add(new SearchStatement(Consts.PRECIO,  precio, SearchOperation.GREATER_THAN_EQUAL));
        }

        return repository.findAll(spec);
    }

}
