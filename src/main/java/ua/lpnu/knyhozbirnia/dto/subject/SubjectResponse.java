package ua.lpnu.knyhozbirnia.dto.subject;

import lombok.Builder;

@Builder
public record SubjectResponse(Integer id, String name) {

}
