package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherRequest;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherWorkResponse;
import ua.lpnu.knyhozbirnia.service.PublisherService;

@RestController
@RequestMapping("/publishers/")
@AllArgsConstructor
public class PublisherController {
    private PublisherService publisherService;

    @GetMapping
    public Slice<PublisherWorkResponse> getPublishers(@PageableDefault(size = 25) Pageable pageable) {
        return publisherService.getAllPublishers(pageable);
    }
    @GetMapping("{id}/")
    public PublisherResponse getPublisher(@PathVariable Integer id) {
        return publisherService.getPublisherById(id);
    }

    @GetMapping("name/{name}/")
    public PublisherResponse getPublisher(@PathVariable String name) {
        return publisherService.getPublisherByName(name);
    }

    @PostMapping
    public PublisherResponse createPublisher(@RequestBody PublisherRequest publisher) {
        return publisherService.addPublisher(publisher);
    }

    @PutMapping("{id}/")
    public PublisherResponse updatePublisher(@PathVariable Integer id, @RequestBody PublisherRequest publisher) {
        return publisherService.updatePublisher(publisher, id);
    }

    @DeleteMapping("{id}/")
    public void deletePublisher(@PathVariable Integer id) {
        publisherService.deletePublisher(id);
    }

}
