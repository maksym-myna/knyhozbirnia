package ua.lpnu.knyhozbirnia.service;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.dto.work.WorkRequest;
import ua.lpnu.knyhozbirnia.dto.work.WorkResponse;
import ua.lpnu.knyhozbirnia.mapper.PublisherMapper;
import ua.lpnu.knyhozbirnia.mapper.WorkMapper;
import ua.lpnu.knyhozbirnia.model.Author;
import ua.lpnu.knyhozbirnia.model.Subject;
import ua.lpnu.knyhozbirnia.model.Work;
import ua.lpnu.knyhozbirnia.repository.WorkRepository;

import java.util.List;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final WorkMapper workMapper;

    private final PublisherService publisherService;
    private final PublisherMapper publisherMapper;
    private final SubjectService subjectService;
    private final AuthorService authorService;
    private final InventoryItemService itemService;

    public Page<WorkResponse> getWorks(Pageable pageable) {
        return workRepository.findAll(pageable).map(workMapper::toResponse);
    }

    public WorkResponse getWork(Integer id) {
        var work = workRepository.findById(id).orElseThrow();
        return workMapper.toResponse(work);
    }

    public WorkResponse addWork(WorkRequest workRequest) {
        Work mappedWork = workMapper.toEntity(workRequest);

        return editWork(workRequest, null);
    }

    public WorkResponse editWork(WorkRequest workRequest, Integer id) {
        Work mappedWork = workMapper.toEntity(workRequest, id);

        if (mappedWork.getLanguage() == null) {
            throw new ConstraintViolationException("Language is required", null);
        }

        if (mappedWork.getPublisher() == null) {
            var publisher = publisherMapper.toEntity(publisherService.addPublisher(workRequest.publisher()));
            mappedWork.setPublisher(publisher);
        }
        if (mappedWork.getSubjects().size() < workRequest.subjects().size()) {
            List<String> mappedSubjects = mappedWork.getSubjects().stream().map(Subject::getName).toList();
            List<SubjectRequest> filteredSubjects = workRequest.subjects().stream()
                    .filter(Predicate.not(subject -> mappedSubjects.contains(subject.name())))
                    .toList();
            var subjects = subjectService.addSubjects(filteredSubjects);
            mappedWork.getSubjects().addAll(subjects);
        }

        if (mappedWork.getAuthors().size() < workRequest.authors().size()) {
            List<String> mappedAuthors = mappedWork.getAuthors().stream().map(Author::getFullName).toList();
            List<AuthorRequest> filteredAuthors = workRequest.authors().stream()
                    .filter(Predicate.not(subject -> mappedAuthors.contains(subject.fullName())))
                    .toList();
            var authors = authorService.addAuthors(filteredAuthors);

            mappedWork.getAuthors().addAll(authors);
        }

        Work savedWork = workRepository.save(mappedWork);

        itemService.addInventoryItems(savedWork, workRequest.copies().medium(), workRequest.copies().quantity());

        return workMapper.toResponse(savedWork);
    }

    public void removeWork(Integer id) {
        var work = workRepository.findById(id).orElseThrow();
        workRepository.delete(work);
    }
}
