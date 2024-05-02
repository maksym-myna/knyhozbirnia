package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherRequest;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherWorkResponse;
import ua.lpnu.knyhozbirnia.mapper.PublisherMapper;
import ua.lpnu.knyhozbirnia.repository.PublisherRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public Slice<PublisherWorkResponse> getAllPublishers(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }

    public PublisherResponse getPublisherById(Integer id) {
        return publisherRepository.findPublisherById(id).orElse(null);
    }

    public PublisherResponse getPublisherByName(String name) {
        return publisherRepository.findByName(name).orElse(null);
    }

    @Transactional
    @Modifying
    public PublisherResponse addPublisher(PublisherRequest publisher) {
        return updatePublisher(publisher, null);
    }

    @Transactional
    @Modifying
    public PublisherResponse updatePublisher(PublisherRequest publisher, Integer id) {
        var mappedPublisher = publisherMapper.toEntity(publisher, id);
        return publisherMapper.toResponse(publisherRepository.save(mappedPublisher));
    }

    @Transactional
    @Modifying
    public void deletePublisher(Integer id) {
        publisherRepository.deleteById(id);
    }
}
