package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.model.Listing;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {}
