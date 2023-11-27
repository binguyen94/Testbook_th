package com.example.testbook_th.Controller.Rest;


import com.example.testbook_th.Service.Book.BookService;
import com.example.testbook_th.Service.Book.Request.BookSaveRequest;
import com.example.testbook_th.Service.Book.Response.BookDetailResponse;
import com.example.testbook_th.Service.Book.Response.BookListResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/books")
@AllArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookListResponse>> list(@PageableDefault(size = 5) Pageable pageable,
                                                       @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(bookService.getAll(pageable, search), HttpStatus.OK);
    }

    @PostMapping
    public void create(@RequestBody BookSaveRequest request){
        bookService.create(request);
    }
    @GetMapping("{id}")
    public ResponseEntity<BookDetailResponse> findById(@PathVariable Long id){
        return new ResponseEntity<>(bookService.findById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateBook(@RequestBody BookSaveRequest request, @PathVariable Long id){
        bookService.update(request,id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Boolean isDeleted = bookService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
