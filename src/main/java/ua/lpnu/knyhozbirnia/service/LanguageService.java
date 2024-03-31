package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.model.Author;
import ua.lpnu.knyhozbirnia.model.Language;
import ua.lpnu.knyhozbirnia.repository.AuthorRepository;
import ua.lpnu.knyhozbirnia.repository.LanguageRepository;

@Service
@AllArgsConstructor
public class LanguageService {
    private final LanguageRepository langRepository;

    public Language getById(String id) {
        return langRepository.findById(id).orElse(null);
    }

}
