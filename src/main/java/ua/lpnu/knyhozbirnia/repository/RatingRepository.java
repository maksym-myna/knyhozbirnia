package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {}
