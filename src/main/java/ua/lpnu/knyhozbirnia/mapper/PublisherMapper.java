package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherRequest;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.model.Author;
import ua.lpnu.knyhozbirnia.model.Publisher;
import ua.lpnu.knyhozbirnia.model.Subject;

@Service
public class PublisherMapper {

    public Publisher toEntity(PublisherRequest publisherRequest){
        return Publisher
                .builder()
                .name(publisherRequest.name())
                .build();
    }

    public Publisher toEntity(PublisherResponse publisherResponse){
        return Publisher
                .builder()
                .id(publisherResponse.id())
                .name(publisherResponse.name())
                .build();
    }

    public PublisherResponse toResponse(Publisher publisher){
        return PublisherResponse
                .builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }
}
