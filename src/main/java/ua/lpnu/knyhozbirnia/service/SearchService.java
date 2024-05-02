package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.language.LanguageResponse;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.dto.search.WorkAuthorSearchResponse;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.repository.*;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SearchService {
    private final WorkRepository workRepository;
    private final AuthorRepository authorRepository;
    private final SubjectRepository subjectRepository;
    private final PublisherRepository publisherRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;

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
        return authorRepository.findByNameContains(searchTerm, pageable);
    }

    public Slice<SubjectResponse> findSubjects(String searchTerm, Pageable pageable) {
        return subjectRepository.findByNameContains(searchTerm, pageable);
    }

    public Slice<PublisherResponse> findPublishers(String searchTerm, Pageable pageable) {
        return publisherRepository.findByNameContains(searchTerm, pageable);
    }

    public Slice<LanguageResponse> findLanguages(String searchTerm, Pageable pageable) {
        return languageRepository.findByNameContains(searchTerm, pageable);
    }

    public Slice<UserResponse> findUsers(String searchTerm, Pageable pageable) {
        return userRepository.findByNameContains(searchTerm, pageable);
    }
}
