package ua.lpnu.knyhozbirnia.model.bigquery;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
    private String datasetName;
    private String projectId;
    private List<Dimension> dimensions;
    private List<Fact> facts;
}
