package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.language.LanguageRequest;
import ua.lpnu.knyhozbirnia.dto.language.LanguageResponse;
import ua.lpnu.knyhozbirnia.service.LanguageService;

@RestController
@RequestMapping("/languages/")
@AllArgsConstructor
public class LanguageController {
    private LanguageService langService;

    @GetMapping
    public Slice<LanguageResponse> getLanguages() {
        return langService.getAllLanguages();
    }
    @GetMapping("{id}/")
    public LanguageResponse getLanguage(@PathVariable String id) {
        return langService.getLanguage(id);
    }

    @PostMapping
    public LanguageResponse addLanguage(@RequestBody LanguageRequest language) {
        return langService.addLanguage(language);
    }

    @PutMapping("{id}/")
    public LanguageResponse editLanguage(@PathVariable String id, @RequestBody LanguageRequest language) {
        return langService.updateLanguage(language, id);
    }

    @DeleteMapping("{id}/")
    public void deleteLanguage(@PathVariable String id) {
        langService.deleteLanguage(id);
    }
}
