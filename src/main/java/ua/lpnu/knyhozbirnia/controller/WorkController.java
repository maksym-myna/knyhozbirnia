package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.work.WorkRequest;
import ua.lpnu.knyhozbirnia.dto.work.WorkResponse;
import ua.lpnu.knyhozbirnia.mapper.WorkMapper;
import ua.lpnu.knyhozbirnia.model.*;
import ua.lpnu.knyhozbirnia.repository.AuthorRepository;
import ua.lpnu.knyhozbirnia.repository.LanguageRepository;
import ua.lpnu.knyhozbirnia.repository.PublisherRepository;
import ua.lpnu.knyhozbirnia.repository.SubjectRepository;
import ua.lpnu.knyhozbirnia.repository.WorkRepository;
import ua.lpnu.knyhozbirnia.service.WorkService;

import java.util.List;

@RestController
@RequestMapping("/works/")
@AllArgsConstructor
public class WorkController {
    private WorkService workService;

    @GetMapping
    public Page<WorkResponse> getWorks(
            @PageableDefault(size = 25) Pageable pageable) {
        return workService.getWorks(pageable);
    }

    @GetMapping("{id}/")
    public WorkResponse getWork(@PathVariable Integer id) {
        return workService.getWork(id);
    }

    @PostMapping
    public WorkResponse addWork(@RequestBody WorkRequest workRequest) {
        return workService.addWork(workRequest);
    }

    @PutMapping("{id}/")
    public WorkResponse editWork(@PathVariable Integer id, @RequestBody WorkRequest workRequest) {
        return workService.editWork(workRequest, id);
    }

    @DeleteMapping("{id}/")
    public void deleteWork(@PathVariable Integer id) {
        workService.removeWork(id);
    }
}
