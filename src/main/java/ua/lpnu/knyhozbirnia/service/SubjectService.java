package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.mapper.SubjectMapper;
import ua.lpnu.knyhozbirnia.model.Subject;
import ua.lpnu.knyhozbirnia.repository.SubjectRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public Subject getByName(String name) {
        return subjectRepository.findByName(name).orElseThrow();
    }

    public List<Subject> getByNames(List<String> names) {
        return subjectRepository.findByNameIn(names);
    }

    public List<Subject> addSubjects(List<SubjectRequest> publishers) {
        var mappedSubjects = publishers.stream().map(subjectMapper::toEntity).toList();
        return subjectRepository.saveAll(mappedSubjects);
    }
}
