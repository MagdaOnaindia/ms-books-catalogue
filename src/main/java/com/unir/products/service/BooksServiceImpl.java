package com.unir.products.service;

import java.util.ArrayList;

import com.unir.products.controller.model.ResponseCodes;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.products.controller.model.BookDto;
import com.unir.products.controller.model.CreateBookRequest;
import com.unir.products.data.BookRepository;
import com.unir.products.data.model.Book;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BooksServiceImpl implements BooksService {

	@Autowired
	private BookRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public List<Book> getBooks(String titulo, String autor, String fechaDePublicacion, String editorial, String categoria, Long isbn, String sinopsis, Double valoracion, Boolean visible, Boolean stock, Double precio) {

		if (StringUtils.hasLength(titulo) 
				|| StringUtils.hasLength(autor) 
				|| StringUtils.hasLength(fechaDePublicacion)
				|| StringUtils.hasLength(editorial)
				|| StringUtils.hasLength(categoria)
				|| isbn != null
				|| StringUtils.hasLength(sinopsis)
				|| valoracion != null
				|| StringUtils.hasLength(fechaDePublicacion)
				|| stock != null
				|| visible != null
				|| precio != null) {
			return repository.search( titulo,  autor,  fechaDePublicacion,  editorial,  categoria,  isbn,  sinopsis,  valoracion,  visible,  stock,  precio);
		}

		List<Book> books = repository.getBooks();
		return books.isEmpty() ? null : books;
	}

	@Override
	public Book getBook(String bookId) {
		return repository.getById(Long.valueOf(bookId));
	}

	@Override
	public Boolean removeBook(String bookId) {

		Book book = repository.getById(Long.valueOf(bookId));

		if (book != null) {
			repository.delete(book);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public ArrayList<Object> createBook(CreateBookRequest request) {
		
		ArrayList<Object> response = new ArrayList<Object>();

		//Otra opcion: Jakarta Validation: https://www.baeldung.com/java-validation
		if (request != null && StringUtils.hasLength(request.getTitulo().trim())
				&& StringUtils.hasLength(request.getAutor().trim())
				&& StringUtils.hasLength(request.getFechaDePublicacion().trim()) 
				&& StringUtils.hasLength(request.getEditorial().trim()) 
				&& StringUtils.hasLength(request.getCategoria().trim()) 
				&& request.getIsbn() != null 
				&& StringUtils.hasLength(request.getPortada().trim()) 
				&& StringUtils.hasLength(request.getSinopsis().trim())  
				&& request.getVisible() != null
				&& request.getStock() != null
				&& request.getPrecio() != null) {
			
			
			//No se encuentra el libro con ISBN
			if(repository.getByIsbn(request.getIsbn()) == null) {
				
				Book book = Book.builder()
						.titulo(request.getTitulo())
						.autor(request.getAutor())
						.fechaDePublicacion(request.getFechaDePublicacion())
						.editorial(request.getEditorial())
						.categoria(request.getCategoria())
						.isbn(request.getIsbn())
						.portada(request.getPortada())
						.sinopsis(request.getSinopsis())
						.stock(request.getStock())
						.visible(request.getVisible())
						.precio(request.getPrecio())
						.build();
				
				
				response.add(repository.save(book));
				response.add(ResponseCodes.OK);
				
			}
			//El libro con ISBN existe
			else {
				response.add(null);
				response.add(ResponseCodes.DUPLICATE);
			}

			
		} else {
			response.add(null);
			response.add(ResponseCodes.BAD_REQUEST);
		}
		
		return response;
	}

	@Override
	public Book updateBook(String bookId, String request) {

		//PATCH se implementa en este caso mediante Merge Patch: https://datatracker.ietf.org/doc/html/rfc7386
		Book book = repository.getById(Long.valueOf(bookId));
		if (book != null) {
			try {
				JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
				JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(book)));
				Book patched = objectMapper.treeToValue(target, Book.class);
				repository.save(patched);
				return patched;
			} catch (JsonProcessingException | JsonPatchException e) {
				log.error("Error updating book {}", bookId, e);
                return null;
            }
        } else {
			return null;
		}
	}

	@Override
	public Book updateBook(String bookId, BookDto updateRequest) {
		Book book = repository.getById(Long.valueOf(bookId));
		if (book != null) {
			book.update(updateRequest);
			repository.save(book);
			return book;
		} else {
			return null;
		}
	}

}
