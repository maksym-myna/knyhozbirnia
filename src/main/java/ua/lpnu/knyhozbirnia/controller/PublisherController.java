package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lpnu.knyhozbirnia.model.Publisher;
import ua.lpnu.knyhozbirnia.repository.PublisherRepository;

import java.util.List;

@RestController
@RequestMapping("/publishers/")
@AllArgsConstructor
public class PublisherController {
    private PublisherRepository publisherRepository;
//    private PublisherMapper publisherMapper;

    @GetMapping
    public List<Publisher> getPublishers() {
        return publisherRepository.findAll();
    }
    @GetMapping("{id}/")
    public Publisher getPublisher(@PathVariable Integer id) {
        return publisherRepository.findById(id).orElseThrow();
    }

}
