package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    String FIND_USERS_QUERY = """
            SELECT
                new ua.lpnu.knyhozbirnia.dto.user.UserResponse(
                    u.id,
                    u.firstName,
                    u.lastName,
                    u.email,
                    u.gender,
                    u.birthday,
                    u.userRole,
                    u.pfpUrl)
                FROM User u
            """;
    String FIND_USER_QUERY_BY_ID_QUERY = FIND_USERS_QUERY + " WHERE u.id = :id";
    String FIND_USER_QUERY_BY_EMAIL_QUERY = FIND_USERS_QUERY + " WHERE u.email = :email";


    @Query(FIND_USER_QUERY_BY_ID_QUERY)
    Optional<UserResponse> findUserById(Integer id);
    @Query(FIND_USER_QUERY_BY_EMAIL_QUERY)
    Optional<UserResponse> findByEmail(String email);
}
