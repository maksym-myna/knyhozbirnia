package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.user.UserRequest;
import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.mapper.UserMapper;
import ua.lpnu.knyhozbirnia.model.User;
import ua.lpnu.knyhozbirnia.model.UserRole;
import ua.lpnu.knyhozbirnia.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserMapper userMapper;

    public User getCurrentUser() {
        String email = getCurrentUsersEmail();
        return getUserByEmail(email).map(userMapper::toEntity).orElseThrow();
    }

    public String getCurrentUsersEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public UserResponse getCurrentUserResponse(){
        return userMapper.toResponse(getCurrentUser());
    }

    public Optional<UserResponse> getUserById(Integer userId){
        return userRepository.findUserById(userId).map(userMapper::setPfp);
    }

    public Optional<UserResponse> getUserByEmail(String email){
        return userRepository.findByEmail(email).map(userMapper::setPfp);
    }

    public Slice<UserRole> getRoles(){
        return new SliceImpl<>(List.of(UserRole.values()));
    }

    @Transactional
    @Modifying
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional
    @Modifying
    public UserResponse updateUser(UserRequest userRequest, Integer userId){
        User user = userRepository.findById(userId).orElseThrow();

        authService.checkEditAuthority(user);

        user.setBirthday(userRequest.birthday());
        user.setEmail(userRequest.email());
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setGender(userRequest.gender());

        return userMapper.toResponse(userRepository.save(user));
    }


    @Transactional
    @Modifying
    public void deleteById(Integer userId){
        User user = userRepository.findById(userId).orElseThrow();

        authService.checkEditAuthority(user);

        userRepository.deleteById(userId);
    }

}
