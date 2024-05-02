package ua.lpnu.knyhozbirnia.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.dto.work.*;
import ua.lpnu.knyhozbirnia.model.*;
import ua.lpnu.knyhozbirnia.service.AuthorService;
import ua.lpnu.knyhozbirnia.service.LanguageService;
import ua.lpnu.knyhozbirnia.service.PublisherService;
import ua.lpnu.knyhozbirnia.service.SubjectService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkMapper {
    private final AuthorService authorService;
    private final SubjectService subjectService;
    private final LanguageService languageService;
    private final LanguageMapper languageMapper;
    private final PublisherService publisherService;
    private final PublisherMapper publisherMapper;


    public Work toEntity(WorkRequest request, Integer id) {
        var authorNames = request.authors().stream().map(AuthorRequest::name).toList();
        var authors = authorService.getByNames(authorNames);


        PublisherResponse requestedPublisher = publisherService.getPublisherByName(request.publisher().name());
        PublisherResponse publisher = requestedPublisher;
        if (requestedPublisher == null) {
            publisher = publisherService.addPublisher(request.publisher());
        }
        var pub = new Publisher(publisher.id(), publisher.name(), Collections.emptySet(), LocalDateTime.now());

        var subjects = subjectService.getSubjectsByNames(request.subjects().stream().map(SubjectRequest::name).toList());
        var language = languageMapper.toEntity(languageService.getLanguage(request.language().id()));
        return Work
                .builder()
                .id(id)
                .pages(request.pages())
                .isbn(request.isbn())
                .title(request.title())
                .weight(request.weight())
                .releaseYear(request.releaseYear())
                .authors(authors)
                .subjects(subjects)
                .language(language)
                .publisher(pub)
                .copies((long)request.quantity())
                .medium(request.medium())
                .currentlyReading(0L)
                .wantToRead(0L)
                .alreadyRead(0L)
                .rating(0.0)
                .scored(0L)
                .build();
    }

    public WorkResponse toResponse(Work work) {
        return WorkResponse
                .builder()
                .id(work.getId())
                .modifiedAt(work.getModifiedAt())
                .pages(work.getPages())
                .isbn(work.getIsbn())
                .language(languageMapper.toNameResponse(work.getLanguage()))
                .title(work.getTitle())
                .weight(work.getWeight())
                .publisher(publisherMapper.toResponse(work.getPublisher()))
                .releaseYear(work.getReleaseYear())
                .authors(work.getAuthors().stream().map(Author::getName).collect(Collectors.toSet()))
                .subjects(work.getSubjects().stream().map(Subject::getName).collect(Collectors.toSet()))
                .wantToRead(work.getWantToRead())
                .currentlyReading(work.getCurrentlyReading())
                .alreadyRead(work.getAlreadyRead())
                .scored(work.getScored())
                .rating(work.getRating())
                .copies(work.getCopies())
                .availableCopies(work.getAvailableCopies())
                .build();
    }
}
