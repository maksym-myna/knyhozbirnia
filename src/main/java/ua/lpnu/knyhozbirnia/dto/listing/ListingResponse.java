package ua.lpnu.knyhozbirnia.dto.listing;

import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.model.ReadingStatus;
import ua.lpnu.knyhozbirnia.model.User;

import java.time.LocalDateTime;

public record ListingResponse(
        Integer id,
        UserResponse user,
        String title,
        String isbn,
        ReadingStatus status,
        LocalDateTime listedAt) {
    public ListingResponse(Integer id, User user, String title, String isbn, ReadingStatus status, LocalDateTime listedAt) {
        this(id, new UserResponse(user), title, isbn, status, listedAt);
    }
}
