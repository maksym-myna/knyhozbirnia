package ua.lpnu.knyhozbirnia.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.dto.work.*;
import ua.lpnu.knyhozbirnia.model.*;
import ua.lpnu.knyhozbirnia.service.AuthorService;
import ua.lpnu.knyhozbirnia.service.LanguageService;
import ua.lpnu.knyhozbirnia.service.PublisherService;
import ua.lpnu.knyhozbirnia.service.SubjectService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class WorkMapper {
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;
    private final LanguageService languageService;
    private final LanguageMapper languageMapper;
    private final PublisherService publisherService;
    private final PublisherMapper publisherMapper;

    public Work toEntity(WorkRequest request) {
        return toEntity(request, null);
    }

    public Work toEntity(WorkRequest request, Integer id) {
        var authors = authorService.getByNames(request.authors().stream().map(AuthorRequest::fullName).toList());
        var subjects = subjectService.getByNames(request.subjects().stream().map(SubjectRequest::name).toList());
        var language = languageService.getById(request.language().id());
        var publisher = publisherService.getByName(request.publisher().name());
        return Work
                .builder()
                .id(id)
                .addedAt(LocalDateTime.now())
                .pages(request.pages())
                .isbn(request.isbn())
                .title(request.title())
                .weight(request.weight())
                .releaseYear(request.releaseYear())
                .authors(authors)
                .subjects(subjects)
                .language(language)
                .publisher(publisher)
                .copies((long)request.copies().quantity())
                .currentlyReading(0L)
                .wantToRead(0.0)
                .alreadyRead(0.0)
                .rating(0.0)
                .scored(0L)
                .build();
    }

    public WorkResponse toResponse(Work work) {
        return WorkResponse
                .builder()
                .id(work.getId())
                .addedAt(work.getAddedAt())
                .pages(work.getPages())
                .isbn(work.getIsbn())
                .language(languageMapper.toResponse(work.getLanguage()))
                .title(work.getTitle())
                .weight(work.getWeight())
                .publisher(publisherMapper.toResponse(work.getPublisher()))
                .releaseYear(work.getReleaseYear())
                .authors(work.getAuthors().stream().map(authorMapper::toResponse).toList())
                .wantToRead(work.getWantToRead())
                .currentlyReading(work.getCurrentlyReading())
                .alreadyRead(work.getAlreadyRead())
                .scored(work.getScored())
                .rating(work.getRating())
                .copies(work.getCopies())
                .subjects(work.getSubjects().stream().map(subjectMapper::toResponse).toList())
//                .ratings(work.getRatings().stream().map(this::ratingToResponse).collect(Collectors.toSet()))
//                .listings(work.getListings().stream().map(this::listingToResponse).collect(Collectors.toSet()))
                .build();
    }

    public WorkRatingResponse ratingToResponse(Rating rating){
        return WorkRatingResponse
                .builder()
                .id(rating.getId())
                .score(rating.getScore())
                .ratedAt(rating.getListedAt())
                .userId(rating.getUser().getId())
                .build();
    }

    public WorkListingResponse listingToResponse(Listing listing){
        return WorkListingResponse
                .builder()
                .id(listing.getId())
                .readingStatus(listing.getReadingStatus())
                .listedAt(listing.getListedAt())
                .userId(listing.getUser().getId())
                .build();
    }
}
