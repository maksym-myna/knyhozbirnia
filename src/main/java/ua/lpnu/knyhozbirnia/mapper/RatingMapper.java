package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.rating.RatingResponse;
import ua.lpnu.knyhozbirnia.model.Rating;

@Service
public class RatingMapper {
    public RatingResponse toResponse (Rating rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getUser(),
                rating.getWork().getTitle(),
                rating.getWork().getIsbn(),
                rating.getScore(),
                rating.getRatedAt()
        );
    }
}
