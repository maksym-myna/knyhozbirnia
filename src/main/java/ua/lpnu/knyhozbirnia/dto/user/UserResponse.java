package ua.lpnu.knyhozbirnia.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.lpnu.knyhozbirnia.model.User;
import ua.lpnu.knyhozbirnia.model.UserRole;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class UserResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDateTime birthday;
    private UserRole role;
    private String pfpUrl;

    public UserResponse(User user) {
        this(user,user.getPfpUrl());
    }

    public UserResponse(User user, String pfpUrl) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender(),
                user.getBirthday(),
                user.getUserRole(),
                pfpUrl
        );
    }
}

