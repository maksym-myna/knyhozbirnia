package ua.lpnu.knyhozbirnia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

@Entity
@Table(name = "work")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Builder
@Indexed
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Integer id;

    @KeywordField
    @NotBlank(message = JpaValidationErrorMessages.NOT_EMPTY_CONSTRAINT_VIOLATION)
    private String title;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Positive(message = JpaValidationErrorMessages.POSITIVE_VALUE_CONSTRAINT_VIOLATION)
    private Integer pages;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Column(name = "release_year")
    private Integer releaseYear;

    @NotBlank(message = JpaValidationErrorMessages.NOT_EMPTY_CONSTRAINT_VIOLATION)
    @Size(min = 13, max = 13, message = JpaValidationErrorMessages.EXACT_LENGTH_VALUE_CONSTRAINT_VIOLATION)
    private String isbn;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write="?::item_medium_type")
    @Column(columnDefinition = "item_medium_type")
    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    private WorkMedium medium;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Positive(message = JpaValidationErrorMessages.POSITIVE_VALUE_CONSTRAINT_VIOLATION)
    private Float weight;

    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    private Language language;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "work_author",
            joinColumns = @JoinColumn(name = "work_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonManagedReference
    List<Author> authors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "work_subject",
            joinColumns = @JoinColumn(name = "work_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @JsonManagedReference
    List<Subject> subjects = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void updateTimestamps() {
        modifiedAt = LocalDateTime.now();
    }

    @Formula("(SELECT COUNT(l.work_id) FROM Listing l WHERE l.work_id = work_id AND l.reading_status = 'CURRENTLY_READING')")
    private Long currentlyReading;

    @Formula("(SELECT COUNT(l.work_id) FROM Listing l WHERE l.work_id = work_id AND l.reading_status = 'WANT_TO_READ')")
    private Long wantToRead;

    @Formula("(SELECT COUNT(l.work_id) FROM Listing l WHERE l.work_id = work_id AND l.reading_status = 'ALREADY_READ')")
    private Long alreadyRead;

    @Formula("(SELECT COALESCE(AVG(r.score),0) FROM rating r WHERE r.work_id = work_id)")
    private Double rating;

    @Formula("(SELECT COUNT(r.score) FROM rating r WHERE r.work_id = work_id)")
    private Long scored;

    @Formula("(SELECT COUNT(ii.work_id) FROM inventory_item ii WHERE ii.work_id = work_id)")
    private Long copies;

    @Formula("(SELECT CASE WHEN w.medium = 'EBOOK' THEN 1 ELSE COUNT(DISTINCT ii.item_id) END FROM work w LEFT JOIN inventory_item ii ON w.work_id = ii.work_id LEFT JOIN Loan l ON ii.item_id = l.item_id LEFT JOIN loan_return lr ON lr.loan_id = l.loan_id WHERE w.work_id = work_id AND ((l.loaned_at IS NULL AND lr.returned_at IS NULL) OR (l.loaned_at IS NOT NULL AND lr.returned_at IS NOT null)) GROUP BY w.medium)")
    private Long availableCopies;

    @JsonIgnore
    @OneToMany(mappedBy = "work", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<InventoryItem> inventoryItems;
}