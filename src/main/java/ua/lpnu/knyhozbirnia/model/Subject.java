package ua.lpnu.knyhozbirnia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.lpnu.knyhozbirnia.contstants.JpaValidationErrorMessages;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "subject")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Integer id;

    @NotBlank(message = JpaValidationErrorMessages.NOT_EMPTY_CONSTRAINT_VIOLATION)
    @Column(name = "subject_name")
    private String name;

    @ManyToMany(mappedBy = "subjects")
    @JsonBackReference
    private Set<Work> works = new HashSet<>();;


//    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
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
