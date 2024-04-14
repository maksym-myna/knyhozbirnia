package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.listing.ListingResponse;
import ua.lpnu.knyhozbirnia.model.Listing;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {
    String SELECT_LISTING_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.listing.ListingResponse(
                l.id,
                l.user,
                l.work.title,
                l.work.isbn,
                l.readingStatus,
                l.listedAt
            ) FROM Listing l
        """;

    String SELECT_USERS_LISTINGS_QUERY = SELECT_LISTING_QUERY + " WHERE l.user.id = :userId";
    String SELECT_LISTINGS_QUERY = SELECT_LISTING_QUERY + " WHERE l.work.id = :workId";
    String SELECT_LISTING_BY_ID = SELECT_LISTING_QUERY + " WHERE l.id = :id";

    @Query(value = SELECT_LISTINGS_QUERY)
    Slice<ListingResponse> getListings(Integer workId, Pageable pageable);
    @Query(value = SELECT_USERS_LISTINGS_QUERY)
    Slice<ListingResponse> getUserListings(Integer userId, Pageable pageable);

    @Query(value = SELECT_LISTING_BY_ID)
    ListingResponse getListing(Integer id);
}
