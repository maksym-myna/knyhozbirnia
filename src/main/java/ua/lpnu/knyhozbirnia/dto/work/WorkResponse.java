package ua.lpnu.knyhozbirnia.dto.work;

import lombok.Builder;
import org.hibernate.annotations.Formula;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.language.LanguageResponse;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record WorkResponse (
    Integer id,
    String title,
    Integer pages,
    Integer releaseYear,
    String isbn,
    Float weight,
    LocalDateTime addedAt,
    PublisherResponse publisher,
    LanguageResponse language,
    List<AuthorResponse> authors,
    List<SubjectResponse> subjects,
//    Set<WorkRatingResponse> ratings,
//    Set<WorkListingResponse> listings,
    Long currentlyReading,
    Double wantToRead,
    Double alreadyRead,
    Double rating,
    Long scored,
    Long copies
    ){
}
