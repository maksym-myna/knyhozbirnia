package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherRequest;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.mapper.PublisherMapper;
import ua.lpnu.knyhozbirnia.model.Language;
import ua.lpnu.knyhozbirnia.model.Publisher;
import ua.lpnu.knyhozbirnia.repository.LanguageRepository;
import ua.lpnu.knyhozbirnia.repository.PublisherRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public Publisher getByName(String name) {
        return publisherRepository.findByName(name).orElse(null);
    }

    public PublisherResponse addPublisher(PublisherRequest publisher) {
        var mappedPublisher = publisherMapper.toEntity(publisher);
        return publisherMapper.toResponse(publisherRepository.save(mappedPublisher));
    }

}
