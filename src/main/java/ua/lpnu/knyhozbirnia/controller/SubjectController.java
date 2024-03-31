package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lpnu.knyhozbirnia.model.Subject;
import ua.lpnu.knyhozbirnia.repository.PublisherRepository;
import ua.lpnu.knyhozbirnia.repository.SubjectRepository;

import java.util.List;

@RestController
@RequestMapping("/subjects/")
@AllArgsConstructor
public class SubjectController {
    private SubjectRepository subjectRepository;
//    private SubjectMapper subjectMapper;

    @GetMapping
    public List<Subject> getSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable).getContent();
    }
    @GetMapping("{id}/")
    public Subject getSubject(@PathVariable Integer id) {
        return subjectRepository.findById(id).orElseThrow();
    }

}
