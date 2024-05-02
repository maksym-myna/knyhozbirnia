package ua.lpnu.knyhozbirnia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "available_copies")
public class AvailableCopies {
    @Id
    @Column(name = "work_id")
    private Long id;

    @Column
    private Long qty;
}