package com.unir.products.controller;


import java.util.ArrayList;
import com.unir.products.controller.model.ResponseCodes;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.unir.products.controller.model.BookDto;
import com.unir.products.controller.model.BookSearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unir.products.data.model.Book;
import com.unir.products.controller.model.CreateBookRequest;
import com.unir.products.service.BooksService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Controlador de catalogo", description = "Microservicio encargado de exponer operaciones sobre los libros en el catalogo: crear, eliminar, modificar (total y parcialmente) y buscar libros (por varios criterios).")
public class BooksController {

    private final BooksService service;

    /* API REST para la busqueda de libros por varios criterios:
       titulo, autor, fecha de publicación, editorial, categoria, isbn, sinopsis, valoración, disponible, en stock o precio.
     */
    @GetMapping("/books")
    @Operation(
            operationId = "Obtener libros con posiblidad de busqueda por todos los campos",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista con los libros encontrados acorde a los criterios seleccionados.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
   public ResponseEntity<List<Book>> getProducts(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "titulo", description = "Titulo del libro (búsqueda parcial)", example = "", required = false)
                 @RequestParam(required = false) String titulo,

            @Parameter(name = "autor", description = "Autor del libro (búsqueda parcial)", example = "", required = false)
                 @RequestParam(required = false) String autor,

            @Parameter(name = "fechaDePublicacionDesde", description = "Fecha de publicación mínima (formato YYYY-MM-DD)", example = "", required = false)
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDePublicacionDesde,

            @Parameter(name = "fechaDePublicacionHasta", description = "Fecha de publicación máxima (formato YYYY-MM-DD)", example = "", required = false)
                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDePublicacionHasta,

            @Parameter(name = "editorial", description = "Editorial del libro (búsqueda parcial)", example = "", required = false)
                 @RequestParam(required = false) String editorial,

            @Parameter(name = "categoria", description = "Categoría del libro (coincidencia exacta)", example = "", required = false)
                 @RequestParam(required = false) String categoria,

            @Parameter(name = "isbn", description = "ISBN del libro (coincidencia exacta o parcial según la lógica del backend)", example = "", required = false)
                 @RequestParam(required = false) String isbn,

            @Parameter(name = "valoracionMin", description = "Valoración mínima del libro (ej. 1.0 a 5.0)", example = "", required = false)
                 @RequestParam(required = false) Double valoracionMin,

            @Parameter(name = "visible", description = "Indica si el libro debe estar visible (true/false)", example = "", required = false)
                 @RequestParam(required = false) Boolean visible,

            @Parameter(name = "conStock", description = "Indica si se deben buscar libros con stock disponible (true para stock > 0)", example = "", required = false)
                 @RequestParam(required = false) Boolean conStock,

            @Parameter(name = "precioMin", description = "Precio mínimo del libro", example = "", required = false)
                 @RequestParam(required = false) Double precioMin,

            @Parameter(name = "precioMax", description = "Precio máximo del libro", example = "", required = false)
                 @RequestParam(required = false) Double precioMax) {

        log.info("headers: {}", headers);
        BookSearchCriteria criteria = new BookSearchCriteria();
        criteria.setTitulo(titulo);
        criteria.setAutor(autor);
        criteria.setFechaDePublicacionDesde(fechaDePublicacionDesde);
        criteria.setFechaDePublicacionHasta(fechaDePublicacionHasta);
        criteria.setEditorial(editorial);
        criteria.setCategoria(categoria);
        criteria.setIsbn(isbn);
        criteria.setValoracionMin(valoracionMin);
        criteria.setVisible(visible);
        criteria.setConStock(conStock);
        criteria.setPrecioMin(precioMin);
        criteria.setPrecioMax(precioMax);
        List<Book> books = service.getBooks(criteria);

        if (books != null && !books.isEmpty()) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /* API REST para la busqueda de un libro por su ID  */
    @GetMapping("/books/{bookId}")
    @Operation(
            operationId = "Encuentra un libro a través de si ID.",
            description = "Operacion de lectura",
            summary = "Devolveremos el libro a partir de su ID.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No existe ningun libro para ese Id.")
    public ResponseEntity<Book> getBook(@PathVariable String bookId) {

        log.info("Recibida petición para libro con Id {}", bookId);
        Book book = service.getBook(bookId);

        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    /* Creamos un nuevo libro: insertar un libro en la base de datos */
    @PostMapping("/books")
    @Operation(
            operationId = "Insertar un libro",
            description = "Operacion de escritura",
            summary = "Creamos un libro a partir de sus datos",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "409",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Ya existe otro libro con ese ISBN.")
    public ResponseEntity<Book> addBook(@RequestBody CreateBookRequest request) {
    	
    	
        ArrayList<Object> response = service.createBook(request);
        ResponseCodes code = (ResponseCodes) response.getLast();
        Book createdBook = (Book) response.getFirst();
        
        switch(code) {
        
        case ResponseCodes.OK:
        	return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        case ResponseCodes.BAD_REQUEST:
        	return ResponseEntity.badRequest().build();
        case ResponseCodes.DUPLICATE:
        	return ResponseEntity.status(HttpStatus.CONFLICT).build();
        default:
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        

    }

    /* Borramos un libro a partir de su identificador */
    @DeleteMapping("/books/{bookId}")
    @Operation(
            operationId = "Eliminar un libro existente",
            description = "Operacion de escritura / borrado",
            summary = "Se elimina un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {

        Boolean removed = service.removeBook(bookId);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /* Modificamos parte del contenido de un libro */
    @PatchMapping("/books/{bookId}")
    @Operation(
            operationId = "Modifica parcialmente un libro (varios campos)",
            description = "Operacion de escritura",
            summary = "Se modifica parcialmente un libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = String.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "libro no válido o datos incorrectos introducidos.")
    public ResponseEntity<Book> patchBook(@PathVariable String bookId, @RequestBody String patchBody) {

        Book patched = service.updateBook(bookId, patchBody);
        if (patched != null) {
            return ResponseEntity.ok(patched);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /* Modificamos un libro completamente (todos los campos) */
    @PutMapping("/books/{bookId}")
    @Operation(
            operationId = "Modifica un libro por completo (todos los campos)",
            description = "Operacion de escritura",
            summary = "Se modifica totalmente el libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a actualizar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = BookDto.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro a modificar no encontrado.")
    public ResponseEntity<Book> updateBook(@PathVariable String bookId, @RequestBody BookDto body) {

        Book updated = service.updateBook(bookId, body);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
