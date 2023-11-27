package com.example.testbook_th.Service.AuthorService;
import com.example.testbook_th.Repository.AuthorRepository;
import com.example.testbook_th.Service.Response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public List<SelectOptionResponse> findAll(){
        return authorRepository.findAll().stream()
                .map(author -> new SelectOptionResponse(author.getId().toString(), author.getName()))
                .collect(Collectors.toList());
    }
}
