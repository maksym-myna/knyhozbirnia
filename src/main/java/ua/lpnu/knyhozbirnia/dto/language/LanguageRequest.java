package ua.lpnu.knyhozbirnia.dto.language;

import lombok.Builder;

@Builder
public record LanguageRequest(String id, String name, Long speakers) {

}

