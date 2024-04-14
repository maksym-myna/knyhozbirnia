package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.service.SubjectService;

@RestController
@RequestMapping("/subjects/")
@AllArgsConstructor
public class SubjectController {
    private SubjectService subjectService;

    @GetMapping
    public Slice<SubjectResponse> getSubjects(
            @PageableDefault(size = 25) Pageable pageable) {
        return subjectService.getAllSubjects(pageable);
    }

    @GetMapping("{id}/")
    public SubjectResponse getSubject(@PathVariable Integer id) {
        return subjectService.getSubjectById(id);
    }

    @GetMapping("name/{name}/")
    public SubjectResponse getSubjectByName(@PathVariable String name) {
        return subjectService.getSubjectByName(name);
    }

    @PostMapping
    public SubjectResponse createSubject(@RequestBody SubjectRequest subject) {
        return subjectService.addSubject(subject);
    }

    @PutMapping("{id}/")
    public SubjectResponse updateSubject(@PathVariable Integer id, @RequestBody SubjectRequest subject) {
        return subjectService.updateSubject(subject, id);
    }

    @DeleteMapping("{id}/")
    public void deleteSubject(@PathVariable Integer id) {
        subjectService.deleteSubject(id);
    }
}
