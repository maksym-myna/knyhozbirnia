package ua.lpnu.knyhozbirnia.dto.rating;

import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.model.User;

import java.time.LocalDateTime;

public record RatingResponse(
        Integer id,
        UserResponse user,
        String title,
        String isbn,
        Integer score,
        LocalDateTime listedAt) {
    public RatingResponse(Integer id, User user, String title, String isbn, Integer score, LocalDateTime listedAt) {
        this(id, new UserResponse(user), title, isbn, score, listedAt);
    }
}
