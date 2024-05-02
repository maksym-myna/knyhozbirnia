package ua.lpnu.knyhozbirnia.model.bigquery;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fact extends Dimension {
    private String title;
    private String definition; // Previously missed
    private List<Metric> metrics;
    private List<Attribute> attributes;
}
