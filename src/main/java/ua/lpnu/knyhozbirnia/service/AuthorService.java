package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.author.AuthorWorkResponse;
import ua.lpnu.knyhozbirnia.mapper.AuthorMapper;
import ua.lpnu.knyhozbirnia.model.Author;
import ua.lpnu.knyhozbirnia.repository.AuthorRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public Slice<AuthorWorkResponse> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public AuthorResponse getAuthorById(Integer id) {
        return authorRepository.findAuthorById(id).orElseThrow();
    }

    public AuthorResponse getAuthorByName(String name) {
        return authorRepository.findByName(name).orElseThrow();
    }

    @Transactional
    @Modifying
    public AuthorResponse addAuthor(AuthorRequest request) {
        return updateAuthor(request, null);
    }

    @Transactional
    @Modifying
    public AuthorResponse updateAuthor(AuthorRequest request, Integer id) {
        var author = authorMapper.toEntity(request, id);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Transactional
    @Modifying
    public void deleteAuthor(Integer id) {
        authorRepository.deleteById(id);
    }

    public List<Author> getByNames(List<String> names) {
        return authorRepository.findByNameIn(names);
    }

    @Transactional
    @Modifying
    public List<Author> addAuthors(List<AuthorRequest> publishers) {
        var mappedAuthors = publishers.stream().map(authorMapper::toEntity).toList();
        return authorRepository.saveAll(mappedAuthors);
    }
}
