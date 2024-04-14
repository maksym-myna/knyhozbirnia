package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.language.LanguageNameResponse;
import ua.lpnu.knyhozbirnia.dto.language.LanguageRequest;
import ua.lpnu.knyhozbirnia.dto.language.LanguageResponse;
import ua.lpnu.knyhozbirnia.model.Language;

@Service
public class LanguageMapper {

    public Language toEntity(LanguageResponse language) {
        return Language
                .builder()
                .id(language.id())
                .name(language.name())
                .speakers(language.speakers())
                .build();
    }

    public Language toEntity(LanguageRequest language) {
        return Language
                .builder()
                .id(language.id())
                .name(language.name())
                .speakers(language.speakers())
                .build();
    }

    public LanguageNameResponse toNameResponse(Language language){
        return LanguageNameResponse
                .builder()
                .id(language.getId())
                .name(language.getName())
                .build();
    }

    public LanguageResponse toResponse(Language language){
        return LanguageResponse
                .builder()
                .id(language.getId())
                .name(language.getName())
                .speakers(language.getSpeakers())
                .build();
    }
}
