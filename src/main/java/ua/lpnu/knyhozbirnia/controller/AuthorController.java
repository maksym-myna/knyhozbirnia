package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lpnu.knyhozbirnia.model.Author;
import ua.lpnu.knyhozbirnia.repository.AuthorRepository;

import java.util.List;

@RestController
@RequestMapping("/authors/")
@AllArgsConstructor
public class AuthorController {
    private AuthorRepository authorRepository;

    @GetMapping
    public List<Author> getAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable).getContent();
    }

    @GetMapping("{id}/")
    public Author getAuthor(@PathVariable Integer id) {
        return authorRepository.findById(id).orElseThrow();
    }

}
