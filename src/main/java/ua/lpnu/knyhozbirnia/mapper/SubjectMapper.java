package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.model.Subject;

@Service
public class SubjectMapper {
    public Subject toEntity(SubjectRequest subjectRequest){
        return Subject
                .builder()
                .name(subjectRequest.name())
                .build();
    }
    public SubjectResponse toResponse(Subject subject){
        return SubjectResponse
                .builder()
                .id(subject.getId())
                .name(subject.getName())
                .build();
    }
}
