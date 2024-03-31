package ua.lpnu.knyhozbirnia.dto.language;

import lombok.Builder;

@Builder
public record LanguageResponse(String id, String name, Long speakers) {

}
