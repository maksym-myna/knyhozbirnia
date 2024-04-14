package ua.lpnu.knyhozbirnia.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

@Entity
@Table(name = "lang")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Language {
    @Id
    @Column(name = "language_id", length = 3, unique=true)
    @Size(min = 3, max = 3, message = JpaValidationErrorMessages.EXACT_LENGTH_VALUE_CONSTRAINT_VIOLATION)
    private String id;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Column(name = "lang_name")
    private String name;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    private Long speakers;

//    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    @JsonBackReference
//    @JsonIgnore
    private Set<Work> works = new HashSet<>();

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
