package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherRequest;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.model.Publisher;

@Service
public class PublisherMapper {

//    public Publisher toEntity(PublisherRequest publisherRequest){
//        return toEntity(publisherRequest, null);
//    }

    public Publisher toEntity(PublisherRequest publisherRequest, Integer id){
        return Publisher
                .builder()
                .id(id)
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
