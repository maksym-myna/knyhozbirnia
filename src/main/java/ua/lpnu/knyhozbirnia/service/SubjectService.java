package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.mapper.SubjectMapper;
import ua.lpnu.knyhozbirnia.model.Subject;
import ua.lpnu.knyhozbirnia.repository.SubjectRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public Slice<SubjectResponse> getAllSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    public SubjectResponse getSubjectById(Integer id) {
        return subjectRepository.findSubjectById(id).orElseThrow();
    }

    public SubjectResponse getSubjectByName(String name) {
        return subjectRepository.findByName(name).orElseThrow();
    }

    @Transactional
    @Modifying
    public SubjectResponse addSubject(SubjectRequest request) {
        return updateSubject(request, null);
    }

    @Transactional
    @Modifying
    public SubjectResponse updateSubject(SubjectRequest request, Integer id) {
        var subject = subjectMapper.toEntity(request, id);
        return subjectMapper.toResponse(subjectRepository.save(subject));
    }

    @Transactional
    @Modifying
    public void deleteSubject(Integer id) {
        subjectRepository.deleteById(id);
    }

    public List<Subject> getSubjectsByNames(List<String> names) {
        return subjectRepository.findByNameIn(names);
    }

    @Transactional
    @Modifying
    public List<Subject> addSubjects(List<SubjectRequest> publishers) {
        var mappedSubjects = publishers.stream().map(subjectMapper::toEntity).toList();
        return subjectRepository.saveAll(mappedSubjects);
    }
}
