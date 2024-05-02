package ua.lpnu.knyhozbirnia.model.bigquery;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    private String title;
    private String type;
    private boolean primaryKey;
    private Attribute subLevel;
    private String foreignKey;
}
