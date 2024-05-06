package ua.lpnu.knyhozbirnia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.config.UserRoleDescription;
import ua.lpnu.knyhozbirnia.dto.work.PartialWorkResponse;
import ua.lpnu.knyhozbirnia.dto.work.WorkRequest;
import ua.lpnu.knyhozbirnia.dto.work.WorkResponse;
import ua.lpnu.knyhozbirnia.model.WorkMedium;
import ua.lpnu.knyhozbirnia.service.AsyncService;
import ua.lpnu.knyhozbirnia.service.WorkService;

import java.util.List;

@RestController
@RequestMapping("/works/")
@AllArgsConstructor
@Tag(name = "Works", description = "Operations on works")
public class WorkController {
    private WorkService workService;
    private final AsyncService asyncService;

    @Operation(summary = "Get a slice of works")
    @GetMapping
    public Slice<PartialWorkResponse> getWorks(
            @PageableDefault(size = 25) Pageable pageable,
            @RequestParam(required = false) List<String> languages,
            @RequestParam(required = false, name="subjects") List<String> subjectNames,
            @RequestParam(required = false, name="publishers") List<String> publisherNames,
            @RequestParam(required = false, name="authors") List<String> authorNames,
            @RequestParam(required = false, name="listings") List<String> listings,
            @RequestParam(required = false) List<Integer> ratings,
            @RequestParam(required = false) List<String> mediums,
            @RequestParam(required = false) Integer minReleaseYear,
            @RequestParam(required = false) Integer maxReleaseYear,
            @RequestParam(required = false) Float minWeight,
            @RequestParam(required = false) Float maxWeight,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false, defaultValue = "true") Boolean isAvailable,
            @RequestParam(required = false, defaultValue = "false") Boolean isAccounted,
            @RequestParam(required = false) Integer userId
    ) {
        return workService.getAllWorks(
                pageable, languages,
                subjectNames,
                publisherNames,
                authorNames,
                listings,
                ratings,
                mediums,
                minReleaseYear,
                maxReleaseYear,
                minWeight,
                maxWeight,
                minPages,
                maxPages,
                isAvailable,
                isAccounted,
                userId
        );
    }

    @Operation(summary = "Get work by id")
    @GetMapping("{id}/")
    public WorkResponse getWork(@PathVariable Integer id) {
        return workService.getWork(id);
    }

    @Operation(summary = "Get all available mediums")
    @GetMapping("mediums/")
    public Slice<WorkMedium> getMediums() {
        return workService.getMediums();
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Add new work")
    @PostMapping
    public WorkResponse addWork(@RequestBody WorkRequest workRequest) {
        var work = workService.addWork(workRequest);
        asyncService.refreshCopiesAsync();
        return work;
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Edit work by id")
    @PutMapping("{id}/")
    public WorkResponse editWork(@PathVariable Integer id, @RequestBody WorkRequest workRequest) {
        return workService.upsertWork(workRequest, id);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Edit work by isbn")
    @UserRoleDescription
    @PutMapping("isbn/{isbn}/")
    public WorkResponse editWork(@PathVariable String isbn, @RequestBody WorkRequest workRequest) {
        return workService.upsertWork(workRequest, isbn);
    }

    @Operation(summary = "Get work by isbn")
    @GetMapping("/isbn/{isbn}/")
    public WorkResponse getWork(@PathVariable String isbn) {
        return workService.getWorkByIsbn(isbn);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Delete work by id")
    @UserRoleDescription
    @DeleteMapping("{id}/")
    public void deleteWork(@PathVariable Integer id) {
        workService.deleteWorkById(id);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Delete work by name")
    @UserRoleDescription
    @DeleteMapping("/isbn/{isbn}/")
    public void deleteWork(@PathVariable String isbn) {
        workService.deleteWorkByIsbn(isbn);
    }
}
