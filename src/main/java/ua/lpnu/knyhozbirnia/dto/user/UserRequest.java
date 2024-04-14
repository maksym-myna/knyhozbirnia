package ua.lpnu.knyhozbirnia.dto.user;

import java.time.LocalDateTime;

public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String gender,
        LocalDateTime birthday
) {}
