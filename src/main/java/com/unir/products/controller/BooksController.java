package com.unir.products.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.unir.products.controller.model.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unir.products.data.model.Book;
import com.unir.products.controller.model.CreateBookRequest;
import com.unir.products.service.BooksService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
                 @Parameter(name = "titulo", description = "Titulo del libro (puede ser parcialmente escrito)", example = "El abismo del olvido", required = false)
                 @RequestParam(required = false) String titulo,
                 @Parameter(name = "autor", description = "Autor del libro", example = "Michael", required = false)
                 @RequestParam(required = false) String autor,
                 @Parameter(name = "fechaDePublicacion", description = "F. Publicación", example = "2025", required = false)
                 @RequestParam(required = false) String fechaDePublicacion,
                 @Parameter(name = "editorial", description = "Editorial", example = "Planeta", required = false)
                 @RequestParam(required = false) String editorial,
                 @Parameter(name = "categoria", description = "Categoria / tipo de libro", example = "Romántica", required = false)
                 @RequestParam(required = false) String categoria,
                 @Parameter(name = "isbn", description = "ISBN", example = "9788408279797", required = false)
                 @RequestParam(required = false) Long isbn,
                 @Parameter(name = "sinopsis", description = "Sinopsis / Resumen", example = "Guerra civil", required = false)
                 @RequestParam(required = false) String sinopsis,
                 @Parameter(name = "valoracion", description = "Valoración de los lectores", example = "4", required = false)
                 @RequestParam(required = false) Double valoracion,
                 @Parameter(name = "visible", description = "Disponible (verdadero/falso)", example = "true", required = false)
                 @RequestParam(required = false) Boolean visible,
                 @Parameter(name = "stock", description = "En Stock / agotado", example = "true", required = false)
                 @RequestParam(required = false) Boolean stock,
                 @Parameter(name = "precio", description = "Precio (euros)", example = "20", required = false)
                 @RequestParam(required = false) Double precio) {

        log.info("headers: {}", headers);
        List<Book> books = service.getBooks(titulo, autor, fechaDePublicacion, editorial, categoria, isbn, sinopsis, valoracion, visible, stock, precio);

        if (books != null) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
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
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Book> addBook(@RequestBody CreateBookRequest request) {

        Book createdBook = service.createBook(request);

        if (createdBook != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } else {
            return ResponseEntity.badRequest().build();
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
