package ua.lpnu.knyhozbirnia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan")
@SecondaryTable(name = "loan_return", pkJoinColumns = @PrimaryKeyJoinColumn(name = "loan_id"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Integer id;

    @NotNull(message = JpaValidationErrorMessages.FOREIGN_KEY_NOT_NULL_CONSTRAINT_VIOLATION)
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @NotNull(message = JpaValidationErrorMessages.FOREIGN_KEY_NOT_NULL_CONSTRAINT_VIOLATION)
    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonBackReference
    private InventoryItem inventoryItem;

    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "loaned_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime loanedAt;

    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "returned_at", table = "loan_return")
    private LocalDateTime returnedAt;

    @PrePersist
    @PreUpdate
    public void updateTimestamps() {
        loanedAt = LocalDateTime.now();
    }
}
