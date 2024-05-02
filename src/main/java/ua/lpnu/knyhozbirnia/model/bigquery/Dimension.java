package ua.lpnu.knyhozbirnia.model.bigquery;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dimension {
    private String title;
    private List<Attribute> attributes;
}
