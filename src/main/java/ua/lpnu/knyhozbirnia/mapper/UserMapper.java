package ua.lpnu.knyhozbirnia.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.config.DefaultProperties;
import ua.lpnu.knyhozbirnia.dto.user.UserRequest;
import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.model.User;

@Service
@AllArgsConstructor
public class UserMapper {
    private final DefaultProperties defaultProperties;

    public User toEntity(UserRequest userRequest) {
        return User.builder().firstName(userRequest.firstName()).lastName(userRequest.lastName())
                .birthday(userRequest.birthday()).email(userRequest.email()).gender(userRequest.gender()).build();
    }

    public User toEntity(UserResponse userResponse) {
        return User.builder()
                .id(userResponse.getId())
                .firstName(userResponse.getFirstName())
                .lastName(userResponse.getLastName())
                .birthday(userResponse.getBirthday())
                .gender(userResponse.getGender())
                .email(userResponse.getEmail())
                .userRole(userResponse.getRole())
                .pfpUrl(userResponse.getPfpUrl())
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user, defaultProperties.getPfp().getOrDefault(user.getGender(), null));
    }
    public UserResponse setPfp(UserResponse user) {
        user.setPfpUrl(defaultProperties.getPfp().getOrDefault(user.getGender(), null));
        return user;
    }
}
