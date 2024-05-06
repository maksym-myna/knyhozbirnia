package ua.lpnu.knyhozbirnia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "author_work_count")
public class AuthorWorkCount {
    @Id
    @Column(name = "full_name")
    private String name;

    @Column
    private String isbn;

    @Column(name = "work_count")
    private Long qty;
}