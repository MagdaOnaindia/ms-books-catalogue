package com.unir.products.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unir.products.data.model.Book;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface BookJpaRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

	List<Book> findByTitulo(String titulo);

	List<Book> findByAutor(String autor);
	
	List<Book> findByEditorial(String editorial);
	
	List<Book> findByCategoria(String categoria);

	List<Book> findByVisible(Boolean visible);
	
	Book findByIsbn(String isbn);

}
