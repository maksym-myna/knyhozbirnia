package ua.lpnu.knyhozbirnia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publisher")
@Data
@Builder
@Indexed
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})

public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Integer id;

    @KeywordField
    @NotBlank(message = JpaValidationErrorMessages.NOT_EMPTY_CONSTRAINT_VIOLATION)
    @Column(name = "publisher_name", unique = true)
    private String name;

    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Work> works = new HashSet<>();

    //    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @PrePersist
    @PreUpdate
    public void updateTimestamps() {
        modifiedAt = LocalDateTime.now();
    }
//    @JsonProperty("work_ids")
//    public Set<Integer> getWorkIds() {
//        return works != null ? works.stream().map(Work::getId).collect(Collectors.toSet()) : null;
//    }
}