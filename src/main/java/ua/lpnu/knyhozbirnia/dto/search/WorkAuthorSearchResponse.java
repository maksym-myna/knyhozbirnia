package ua.lpnu.knyhozbirnia.dto.search;

import org.springframework.data.domain.Pageable;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.work.WorkSearchResponse;

import java.util.List;

public record WorkAuthorSearchResponse (
        List<WorkSearchResponse> works,
        List<AuthorResponse> authors,
        int numberOfElements,
        boolean hasNext,
        boolean hasPrevious,
        Pageable pageable
) {
    public WorkAuthorSearchResponse(List<WorkSearchResponse> works, List<AuthorResponse> authors, boolean hasNext, boolean hasPrevious, Pageable pageable) {
        this(works, authors, works.size() + authors.size(), hasNext, hasPrevious, pageable);
    }
}
