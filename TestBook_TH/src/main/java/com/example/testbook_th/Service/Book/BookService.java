package com.example.testbook_th.Service.Book;

import com.example.testbook_th.Domain.Author;
import com.example.testbook_th.Domain.Book;
import com.example.testbook_th.Domain.BookAuthor;
import com.example.testbook_th.Domain.Category;
import com.example.testbook_th.Repository.BookAuthorRepository;
import com.example.testbook_th.Repository.BookRepository;
import com.example.testbook_th.Service.Book.Request.BookSaveRequest;
import com.example.testbook_th.Service.Book.Response.BookDetailResponse;
import com.example.testbook_th.Service.Book.Response.BookListResponse;
import com.example.testbook_th.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookAuthorRepository bookAuthorRepository;
    public Page<BookListResponse> getAll(Pageable pageable, String search){
        search = "%" + search + "%";
        return bookRepository.searchBooks(search ,pageable).map(e -> {
            var result = AppUtil.mapper.map(e, BookListResponse.class);
            result.setCategory(e.getCategory().getName());
            result.setBookAuthor(e.getBookAuthors()
                    .stream().map(c -> c.getAuthor().getName())
                    .collect(Collectors.joining(", ")));
            return result;
        });
    }
    public void create(BookSaveRequest request){
        var book = AppUtil.mapper.map(request, Book.class);
        book = bookRepository.save(book);
        Book finalBook = book;
//        BookAuthor author1 = new BookAuthor(book, new Author());
//        BookAuthor author2 = new BookAuthor(book, new Author());
//
//        // [autho1, author2]

        bookAuthorRepository.saveAll(request
                .getIdAuthors()
                .stream()
                .map(id -> new BookAuthor(finalBook, new Author(Long.valueOf(id))))
                .collect(Collectors.toList()));
    }
    public BookDetailResponse findById(Long id){
        var book = bookRepository.findById(id).orElse(new Book());
        var result = AppUtil.mapper.map(book, BookDetailResponse.class);
        result.setCategoryId(book.getCategory().getId());
        result.setAuthorIds(book
                .getBookAuthors()
                .stream().map(bookAuthor -> bookAuthor.getAuthor().getId())
                .collect(Collectors.toList()));
        return result;
    }
    public void update(BookSaveRequest request, Long id){
        var bookDb = bookRepository.findById(id).orElse(new Book());
        bookDb.setCategory(new Category());
        AppUtil.mapper.map(request,bookDb);
        bookAuthorRepository.deleteAll(bookDb.getBookAuthors());

        var bookAuthors = new ArrayList<BookAuthor>();
        for (String idAuthor : request.getIdAuthors()) {
            Author author = new Author(Long.valueOf(idAuthor));
            bookAuthors.add(new BookAuthor(bookDb, author));
        }
        bookAuthorRepository.saveAll(bookAuthors);
        bookRepository.save(bookDb);
    }
    public Boolean delete(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            bookAuthorRepository.deleteBookAuthorsByBookId(id);
            bookRepository.deleteById(id);
            return true;
        } else {
            return false; // Không tìm thấy phòng để xóa
        }
    }

}
