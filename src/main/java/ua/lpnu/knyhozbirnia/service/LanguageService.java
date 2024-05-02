package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.language.LanguageRequest;
import ua.lpnu.knyhozbirnia.dto.language.LanguageResponse;
import ua.lpnu.knyhozbirnia.mapper.LanguageMapper;
import ua.lpnu.knyhozbirnia.model.Language;
import ua.lpnu.knyhozbirnia.repository.LanguageRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LanguageService {
    private final LanguageRepository langRepository;
    private final LanguageMapper langMapper;

    public Slice<LanguageResponse> getAllLanguages() {
        Pageable wholePage = Pageable.unpaged();
        return langRepository.findAll(wholePage);
    }

    public LanguageResponse getLanguage(String id) {
        return langRepository.findLanguageById(id).orElseThrow();
    }

    public LanguageResponse getLanguageByName(String name) {
        return langRepository.findLanguageByName(name).orElseThrow();
    }

    @Transactional
    @Modifying
    public LanguageResponse addLanguage(LanguageRequest language) {
        Language lang = langMapper.toEntity(language);
        langRepository.save(lang);
        return langMapper.toResponse(lang);
    }

    @Transactional
    @Modifying
    public LanguageResponse updateLanguage(LanguageRequest languageRequest, String id) {
        Language foundLanguage = langRepository.findById(id).orElseThrow();
        Language mappedLanguage = langMapper.toEntity(languageRequest);
        Language savedLanguage = langRepository.save(mappedLanguage);
        return langMapper.toResponse(savedLanguage);
    }

    @Transactional
    @Modifying
    public void deleteLanguage(String id) {
        langRepository.deleteById(id);
    }
}
