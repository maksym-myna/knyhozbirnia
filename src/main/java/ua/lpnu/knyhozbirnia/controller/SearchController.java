package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.language.LanguageResponse;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.dto.search.WorkAuthorSearchResponse;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.service.SearchService;

@RestController
@RequestMapping("/search/")
@AllArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public WorkAuthorSearchResponse search (
            @RequestParam String q,
            @PageableDefault Pageable pageable) {
        return searchService.findWorksAndAuthors(q, pageable);
    }

    @GetMapping("publishers/")
    public Slice<PublisherResponse> searchForPublishers (
            @RequestParam String q,
            @PageableDefault Pageable pageable) {
        return searchService.findPublishers(q, pageable);
    }

    @GetMapping("authors/")
    public Slice<AuthorResponse> searchForAuthors (
            @RequestParam String q,
            @PageableDefault Pageable pageable) {
        return searchService.findAuthors(q, pageable);
    }

    @GetMapping("subjects/")
    public Slice<SubjectResponse> searchForSubjects (
            @RequestParam String q,
            @PageableDefault Pageable pageable) {
        return searchService.findSubjects(q, pageable);
    }

    @GetMapping("languages/")
    public Slice<LanguageResponse> searchForLanguages (
            @RequestParam String q,
            @PageableDefault Pageable pageable) {
        return searchService.findLanguages(q, pageable);
    }

    @GetMapping("users/")
    public Slice<UserResponse> searchForUsers (
            @RequestParam String q,
            @PageableDefault Pageable pageable) {
        return searchService.findUsers(q, pageable);
    }
}
