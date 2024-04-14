package ua.lpnu.knyhozbirnia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

import java.time.LocalDateTime;

@Entity
@Table(name = "listing")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listing_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write="?::reading_status_type")
    @Column(columnDefinition = "reading_status_type")
    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    private ReadingStatus readingStatus;

//    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "listed_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime listedAt;

    @NotNull(message = JpaValidationErrorMessages.FOREIGN_KEY_NOT_NULL_CONSTRAINT_VIOLATION)
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @NotNull(message = JpaValidationErrorMessages.FOREIGN_KEY_NOT_NULL_CONSTRAINT_VIOLATION)
    @ManyToOne
    @JoinColumn(name = "work_id")
    @JsonBackReference
    private Work work;

//    @PrePersist
//    @PreUpdate
//    public void updateTimestamps() {
//        listedAt = LocalDateTime.now();
//    }

//    @JsonProperty("user_id")
//    public Integer getUserId() {
//        return user != null ? user.getId() : null;
//    }
//
//    @JsonProperty("work_id")
//    public Integer getWorkId() {
//        return work != null ? work.getId() : null;
//    }
}

