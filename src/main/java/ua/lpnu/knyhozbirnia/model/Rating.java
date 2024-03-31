package ua.lpnu.knyhozbirnia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

import java.time.LocalDateTime;
@Entity
@Table(name = "rating")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Integer id;

    @NotNull(message=JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Min(value = 1, message = JpaValidationErrorMessages.MINIMUM_SIZE_CONSTRAINT_VIOLATION)
    @Max(value = 5, message = JpaValidationErrorMessages.MAXIMUM_SIZE_CONSTRAINT_VIOLATION)
    private Integer score;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "rated_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime listedAt;

    @ManyToOne
    @NotNull(message = JpaValidationErrorMessages.FOREIGN_KEY_NOT_NULL_CONSTRAINT_VIOLATION)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "work_id")
    @NotNull(message = JpaValidationErrorMessages.FOREIGN_KEY_NOT_NULL_CONSTRAINT_VIOLATION)
    @JsonBackReference
    private Work work;

    @JsonProperty("user_id")
    public Integer getUserId() {
        return user != null ? user.getId() : null;
    }

    @JsonProperty("work_id")
    public Integer getWorkId() {
        return work != null ? work.getId() : null;
    }
}
