package ua.lpnu.knyhozbirnia.dto.author;

import lombok.Builder;

@Builder
public record AuthorResponse(Integer id, String name) {

}