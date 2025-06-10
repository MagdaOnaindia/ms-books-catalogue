package com.unir.products.service;


import java.util.ArrayList;
import com.unir.products.controller.model.CreateBookRequest;
import com.unir.products.controller.model.BookSearchCriteria;

import java.util.List;

import com.unir.products.data.model.Book;
import com.unir.products.controller.model.BookDto;
import com.unir.products.controller.model.CreateBookRequest;

public interface BooksService {

	List<Book> getBooks(BookSearchCriteria criteria);
	Book getBook(String bookId);
	
	Boolean removeBook(String bookId);
	
	ArrayList<Object> createBook(CreateBookRequest request);

	Book updateBook(String bookId, String updateRequest);

	Book updateBook(String bookId, BookDto updateRequest);

}
