package ua.lpnu.knyhozbirnia.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "library_user")
@SecondaryTable(name = "pfp", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @NotBlank(message = JpaValidationErrorMessages.NOT_EMPTY_CONSTRAINT_VIOLATION)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = JpaValidationErrorMessages.NOT_EMPTY_CONSTRAINT_VIOLATION)
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Pattern(regexp = "[f|m|n]", message = JpaValidationErrorMessages.GENDER_CONSTRAINT_VIOLATION)
    @Column(name = "gender")
    private String gender;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Email(message = JpaValidationErrorMessages.EMAIL_CONSTRAINT_VIOLATION)
    @Column(name = "email", unique = true)
    private String email;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write="?::user_role_domain")
    @Column(columnDefinition = "user_role_domain", name = "role")
    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    private UserRole userRole;

//    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(name = "pfp_url", table = "pfp")
    private String pfpUrl;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Listing> listings = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Rating> ratings = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Loan> loans = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void updateTimestamps() {
        modifiedAt = LocalDateTime.now();
    }
}
