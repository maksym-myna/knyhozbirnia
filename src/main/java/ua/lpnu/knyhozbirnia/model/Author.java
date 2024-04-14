package ua.lpnu.knyhozbirnia.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

@Entity
@Table(name = "author")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Builder
@Indexed
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Integer id;

    @KeywordField
    @NotBlank(message = JpaValidationErrorMessages.NOT_EMPTY_CONSTRAINT_VIOLATION)
    @Column(name = "full_name", unique = true)
    private String name;

//    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @ManyToMany(mappedBy = "authors")
    @JsonBackReference
    private Set<Work> works = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void updateTimestamps() {
        modifiedAt = LocalDateTime.now();
    }

//    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
//    @JsonBackReference
//    private Set<Work> works = new HashSet<>();
//
//    @JsonBackReference
//    public Set<Work> getWorks(){
//        return works;
//    }


//    @JsonProperty("work_ids")
//    public Set<Integer> getWorkIds() {
//        return works != null ? works.stream().map(Work::getId).collect(Collectors.toSet()) : null;
//    }
}
