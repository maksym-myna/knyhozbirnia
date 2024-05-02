package ua.lpnu.knyhozbirnia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "publisher_work_count")
public class PublisherWorkCount {
    @Id
    @Column(name = "publisher_name")
    private String name;

    @Column(name = "work_count")
    private Long qty;
}