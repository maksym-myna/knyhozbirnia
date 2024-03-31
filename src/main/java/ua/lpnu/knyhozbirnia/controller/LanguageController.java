package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lpnu.knyhozbirnia.model.Language;
import ua.lpnu.knyhozbirnia.repository.LanguageRepository;
import ua.lpnu.knyhozbirnia.repository.PublisherRepository;
import ua.lpnu.knyhozbirnia.repository.SubjectRepository;

import java.util.List;

@RestController
@RequestMapping("/languages/")
@AllArgsConstructor
public class LanguageController {
    private LanguageRepository langRepository;
//    private LanguageMapper langMapper;

    @GetMapping
    public List<Language> getLanguages() {
        return langRepository.findAll();
    }
    @GetMapping("{id}/")
    public Language getLanguage(@PathVariable String id) {
        return langRepository.findById(id).orElseThrow();
    }

}
