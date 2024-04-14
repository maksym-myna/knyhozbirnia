package ua.lpnu.knyhozbirnia.dto.work;

import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.language.LanguageIdRequest;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherRequest;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.model.WorkMedium;

import java.util.List;

public record WorkRequest(
        String title,
        Integer releaseYear,
        Integer pages,
        Float weight,
        String isbn,
        Integer quantity,
        WorkMedium medium,
        LanguageIdRequest language,
        PublisherRequest publisher,
        List<AuthorRequest> authors,
        List<SubjectRequest> subjects) {
}
