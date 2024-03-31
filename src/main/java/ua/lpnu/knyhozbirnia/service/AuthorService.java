package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.mapper.AuthorMapper;
import ua.lpnu.knyhozbirnia.model.Author;
import ua.lpnu.knyhozbirnia.repository.AuthorRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public Author getByName(String name) {
        return authorRepository.findByFullName(name).orElseThrow();
    }

    public List<Author> getByNames(List<String> names) {
        return authorRepository.findByFullNameIn(names);
    }

    public List<Author> addAuthors(List<AuthorRequest> publishers) {
        var mappedAuthors = publishers.stream().map(authorMapper::toEntity).toList();
        return authorRepository.saveAll(mappedAuthors);
    }

}
