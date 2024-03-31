package ua.lpnu.knyhozbirnia.dto.language;

import lombok.Builder;

@Builder
public record LanguageNameResponse(String id, String name) {}
