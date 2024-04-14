package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.work.PartialWorkResponse;
import ua.lpnu.knyhozbirnia.dto.work.WorkRequest;
import ua.lpnu.knyhozbirnia.dto.work.WorkResponse;
import ua.lpnu.knyhozbirnia.model.WorkMedium;
import ua.lpnu.knyhozbirnia.service.WorkService;

import java.util.List;

@RestController
@RequestMapping("/works/")
@AllArgsConstructor
public class WorkController {
    private WorkService workService;

    @GetMapping
    public Slice<PartialWorkResponse> getWorks(
            @PageableDefault(size = 25) Pageable pageable,
            @RequestParam(required = false) List<Integer> languageIds,
            @RequestParam(required = false) List<String> subjectNames,
            @RequestParam(required = false) List<String> publisherNames,
            @RequestParam(required = false) List<String> authorNames,
            @RequestParam(required = false) List<WorkMedium> mediums,
            @RequestParam(required = false) Integer minReleaseYear,
            @RequestParam(required = false) Integer maxReleaseYear,
            @RequestParam(required = false) Float minWeight,
            @RequestParam(required = false) Float maxWeight,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false, defaultValue = "true") Boolean isAvailable,
            @RequestParam(required = false, defaultValue = "false") Boolean hasCopies
    ) {
        return workService.getAllWorks(
                pageable,
                languageIds,
                subjectNames,
                publisherNames,
                authorNames,
                mediums,
                minReleaseYear,
                maxReleaseYear,
                minWeight,
                maxWeight,
                minPages,
                maxPages,
                isAvailable,
                hasCopies
        );
    }

    @GetMapping("{id}/")
    public WorkResponse getWork(@PathVariable Integer id) {
        return workService.getWork(id);
    }

    @GetMapping("mediums/")
    public Slice<WorkMedium> getMediums() {
        return workService.getMediums();
    }

    @PostMapping
    public WorkResponse addWork(@RequestBody WorkRequest workRequest) {
        return workService.addWork(workRequest);
    }

    @PutMapping("{id}/")
    public WorkResponse editWork(@PathVariable Integer id, @RequestBody WorkRequest workRequest) {
        return workService.upsertWork(workRequest, id);
    }

    @DeleteMapping("{id}/")
    public void deleteWork(@PathVariable Integer id) {
        workService.deleteWorkById(id);
    }

    @GetMapping("/isbn/{isbn}/")
    public WorkResponse getWork(@PathVariable String isbn) {
        return workService.getWorkByIsbn(isbn);
    }
    @DeleteMapping("/isbn/{isbn}/")
    public void deleteWork(@PathVariable String isbn) {
        workService.deleteWorkByIsbn(isbn);
    }
}
