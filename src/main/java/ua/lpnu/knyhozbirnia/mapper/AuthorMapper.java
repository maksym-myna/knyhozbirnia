package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.model.Author;

@Service
public class AuthorMapper {
    public Author toEntity(AuthorRequest authorRequest){
        return toEntity(authorRequest, null);
    }

    public Author toEntity(AuthorRequest authorRequest, Integer id){
        return Author
                .builder()
                .id(id)
                .name(authorRequest.name())
//                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public AuthorResponse toResponse(Author author){
        return AuthorResponse
                .builder()
                .id(author.getId())
                .name(author.getName())
                .build();
    }
}
