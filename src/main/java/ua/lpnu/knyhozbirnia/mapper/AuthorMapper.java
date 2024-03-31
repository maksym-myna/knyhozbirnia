package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.model.Author;

import java.time.LocalDateTime;

@Service
public class AuthorMapper {

    public Author toEntity(AuthorRequest authorRequest){
        return Author
                .builder()
                .fullName(authorRequest.fullName())
                .addedAt(LocalDateTime.now())
                .build();
    }

    public AuthorResponse toResponse(Author author){
        return AuthorResponse
                .builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }
}
