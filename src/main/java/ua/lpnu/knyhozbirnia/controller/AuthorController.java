package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.author.AuthorWorkResponse;
import ua.lpnu.knyhozbirnia.service.AuthorService;

@RestController
@RequestMapping("/authors/")
@AllArgsConstructor
public class AuthorController {
    private AuthorService authorService;

    @GetMapping
    public Slice<AuthorWorkResponse> getAuthors(
            @PageableDefault(size = 25) Pageable pageable) {
        return authorService.getAllAuthors(pageable);
    }

    @GetMapping("{id}/")
    public AuthorResponse getAuthor(@PathVariable Integer id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping("name/{name}/")
    public AuthorResponse getAuthorByName(@PathVariable String name) {
        return authorService.getAuthorByName(name);
    }

    @PostMapping
    public AuthorResponse createAuthor(@RequestBody AuthorRequest author) {
        return authorService.addAuthor(author);
    }

    @PutMapping("{id}/")
    public AuthorResponse updateAuthor(@PathVariable Integer id, @RequestBody AuthorRequest author) {
        return authorService.updateAuthor(author, id);
    }

    @DeleteMapping("{id}/")
    public void deleteAuthor(@PathVariable Integer id) {
        authorService.deleteAuthor(id);
    }
}
