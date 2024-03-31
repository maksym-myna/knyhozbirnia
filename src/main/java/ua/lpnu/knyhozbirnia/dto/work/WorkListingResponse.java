package ua.lpnu.knyhozbirnia.dto.work;

import lombok.Builder;
import ua.lpnu.knyhozbirnia.model.ReadingStatus;
import ua.lpnu.knyhozbirnia.model.User;
import ua.lpnu.knyhozbirnia.model.Work;

import java.time.LocalDateTime;

@Builder
public record WorkListingResponse(
        Integer id,
        ReadingStatus readingStatus,
        LocalDateTime listedAt,
        Integer userId
) {

}
