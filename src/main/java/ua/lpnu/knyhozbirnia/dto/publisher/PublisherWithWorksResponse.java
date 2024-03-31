package ua.lpnu.knyhozbirnia.dto.publisher;

import lombok.Builder;
import ua.lpnu.knyhozbirnia.dto.work.WorkResponse;

import java.util.List;
import java.util.Set;

@Builder
public record PublisherWithWorksResponse(Integer id, String name, List<WorkResponse> works) {

}
