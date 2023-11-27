package com.example.testbook_th.Controller;

import com.example.testbook_th.Service.AuthorService.AuthorService;
import com.example.testbook_th.Service.CategoryService.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("/book")
@AllArgsConstructor
public class HomeController {

    private final AuthorService authorService;
    private final CategoryService categoryService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("index");
        view.addObject("authors", authorService.findAll());
        view.addObject("categories", categoryService.findAll());
        return view;
    }
}
