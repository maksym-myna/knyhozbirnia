package ua.lpnu.knyhozbirnia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

@Entity
@Table(name = "work")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Integer id;

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

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Positive(message = JpaValidationErrorMessages.POSITIVE_VALUE_CONSTRAINT_VIOLATION)
    private Float weight;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent(message = JpaValidationErrorMessages.PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION)
    @Column(name = "added_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime addedAt;

    @NotNull(message = JpaValidationErrorMessages.NOT_NULL_CONSTRAINT_VIOLATION)
    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "language_id")
    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    private Language language;

    @ManyToMany
    @JoinTable(
            name = "work_author",
            joinColumns = @JoinColumn(name = "work_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonManagedReference
    List<Author> authors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "work_subject",
            joinColumns = @JoinColumn(name = "work_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @JsonManagedReference
    List<Subject> subjects = new ArrayList<>();

//    @OneToMany(mappedBy = "work")
//    @JsonManagedReference
//    private List<Rating> ratings = new ArrayList<>();

//    @OneToMany(mappedBy = "work")
//    @JsonManagedReference
//    private List<Listing> listings = new ArrayList<>();

//    @OneToMany(mappedBy = "work")
//    @JsonManagedReference
//    private List<InventoryItem> copies = new ArrayList<>();

    @Formula("(SELECT COUNT(l.work_id) FROM Listing l WHERE l.work_id = work_id AND l.reading_status = 'CURRENTLY_READING')")
    private Long currentlyReading;

    @Formula("(SELECT COUNT(l.work_id) FROM Listing l WHERE l.work_id = work_id AND l.reading_status = 'WANT_TO_READ')")
    private Double wantToRead;

    @Formula("(SELECT COUNT(l.work_id) FROM Listing l WHERE l.work_id = work_id AND l.reading_status = 'ALREADY_READ')")
    private Double alreadyRead;

    @Formula("(SELECT COALESCE(AVG(r.score),0) FROM rating r WHERE r.work_id = work_id)")
    private Double rating;

    @Formula("(SELECT COUNT(r.score) FROM rating r WHERE r.work_id = work_id)")
    private Long scored;

    @Formula("(SELECT COUNT(ii.work_id) FROM inventory_item ii WHERE ii.work_id = work_id)")
    private Long copies;
}