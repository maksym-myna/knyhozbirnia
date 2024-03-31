package ua.lpnu.knyhozbirnia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan")
@SecondaryTable(name = "loan_return", pkJoinColumns = @PrimaryKeyJoinColumn(name = "loan_id"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull(message = JpaValidationErrorMessages.FOREIGN_KEY_NOT_NULL_CONSTRAINT_VIOLATION)
    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "loaned_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime loanedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "returned_at", table = "loan_return")
    private LocalDateTime returnedAt;

    @JsonProperty("user_id")
    public Integer getUserId() {
        return user != null ? user.getId() : null;
    }

    @JsonProperty("item_id")
    public Integer getItemId() {
        return inventoryItem != null ? inventoryItem.getId() : null;
    }
}
