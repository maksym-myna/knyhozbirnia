package ua.lpnu.knyhozbirnia.dto.work;

import lombok.Builder;
import ua.lpnu.knyhozbirnia.dto.language.LanguageNameResponse;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.model.*;

import java.time.LocalDateTime;
import java.util.*;

@Builder
public record WorkResponse (
    Integer id,
    String title,
    Integer pages,
    Integer releaseYear,
    String isbn,
    WorkMedium medium,
    Float weight,
    LocalDateTime modifiedAt,
    PublisherResponse publisher,
    LanguageNameResponse language,
    Set<String> authors,
    Set<String> subjects,
//    Set<WorkRatingResponse> ratings,
//    Set<WorkListingResponse> listings,
    Long currentlyReading,
    Long wantToRead,
    Long alreadyRead,
    Double rating,
    Long scored,
    Long copies,
    Long availableCopies
    ){

    public WorkResponse(
            Integer id,
            String title,
            Integer pages,
            Integer releaseYear,
            String isbn,
            WorkMedium medium,
            Float weight,
            LocalDateTime modifiedAt,
            Integer publisherId,
            String publisherName,
            String languageId,
            String languageName,
            String authors,
            String subjects,
            Long currentlyReading,
            Long wantToRead,
            Long alreadyRead,
            Double rating,
            Long scored,
            Long copies,
            Long availableCopies
    ) {
        this(
            id,
            title,
            pages,
            releaseYear,
            isbn,
            medium,
            weight,
            modifiedAt,
            new PublisherResponse(publisherId, publisherName),
            new LanguageNameResponse(languageId, languageName),
            authors == null ? Collections.emptySet() : new HashSet<>(Arrays.asList(authors.split(","))),
            subjects == null ? Collections.emptySet() : new HashSet<>(Arrays.asList(subjects.split(","))),
            currentlyReading,
            wantToRead,
            alreadyRead,
            rating,
            scored,
            copies,
            availableCopies
        );
    }
}
