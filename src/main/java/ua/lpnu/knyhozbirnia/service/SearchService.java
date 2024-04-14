package ua.lpnu.knyhozbirnia.service;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.dto.search.WorkAuthorSearchResponse;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.mapper.AuthorMapper;
import ua.lpnu.knyhozbirnia.mapper.PublisherMapper;
import ua.lpnu.knyhozbirnia.mapper.SubjectMapper;
import ua.lpnu.knyhozbirnia.mapper.WorkMapper;
import ua.lpnu.knyhozbirnia.model.Author;
import ua.lpnu.knyhozbirnia.model.Publisher;
import ua.lpnu.knyhozbirnia.model.Subject;
import ua.lpnu.knyhozbirnia.repository.AuthorRepository;
import ua.lpnu.knyhozbirnia.repository.WorkRepository;

import java.util.function.Function;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SearchService {
    private final EntityManager entityManager;
    private final AuthorMapper authorMapper;
    private final PublisherMapper publisherMapper;
    private final SubjectMapper subjectMapper;
    private final WorkRepository workRepository;
    private final AuthorRepository authorRepository;

    public WorkAuthorSearchResponse findWorksAndAuthors(String searchTerm, Pageable pageable) {
        var works = workRepository.findTitlesAndIdsByTitleContains(searchTerm, pageable);

        int pageSize = pageable.getPageSize();
        Pageable authorPageable =
                pageSize >= works.getSize()/2
                        ? Pageable.ofSize(pageSize / 2 - 1)
                        : Pageable.ofSize(pageSize - works.getSize());
        var authors = authorRepository.findByNameContains(searchTerm, authorPageable);

        return new WorkAuthorSearchResponse(works.get().limit(pageSize - authors.getSize()).toList(), authors.getContent(), authors.hasNext() || works.hasNext(), authors.hasPrevious() || works.hasPrevious(), pageable);
    }

    public Slice<AuthorResponse> findAuthors(String searchTerm, Pageable pageable) {
        return findEntities(searchTerm, pageable, Author.class, authorMapper::toResponse, "name");
    }

    public Slice<SubjectResponse> findSubjects(String searchTerm, Pageable pageable) {
        return findEntities(searchTerm, pageable, Subject.class, subjectMapper::toResponse, "name");
    }

    public Slice<PublisherResponse> findPublishers(String searchTerm, Pageable pageable) {
        return findEntities(searchTerm, pageable, Publisher.class, publisherMapper::toResponse, "name");
    }

    private <T, R> Slice<R> findEntities(String searchTerm, Pageable pageable, Class<T> entityClass, Function<T, R> mapper, String fieldPath) {
        SearchSession searchSession = Search.session(entityManager);

        LuceneSearchQuery<T> query = searchSession
                .search(entityClass)
                .extension(LuceneExtension.get())
                .where(f -> f.bool()
                        .should(f.match().field(fieldPath).matching(searchTerm).fuzzy())
                        .should(f.wildcard().field(fieldPath).matching("*" + searchTerm + "*"))
                )
                .toQuery();

        var pageNumber = pageable.getPageNumber();
        var pageSize = pageable.getPageSize();

        return new SliceImpl<>(query
                .fetchAllHits()
                .stream()
                .map(mapper)
                .skip((long)pageNumber * pageSize)
                .limit(pageSize)
                .toList());
    }
}
