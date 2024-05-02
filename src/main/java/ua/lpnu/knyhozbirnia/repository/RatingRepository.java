package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.rating.RatingResponse;
import ua.lpnu.knyhozbirnia.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    String SELECT_RATING_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.rating.RatingResponse(
                r.id,
                r.user,
                r.work.title,
                r.work.isbn,
                r.score,
                r.ratedAt
            ) FROM Rating r
        """;

    String FILTER_BY_ID = "r.user.id = :userId";
    String FILTER_BY_WORK = "r.work.id = :workId";

    String SELECT_RATINGS_BY_WORK_ID_QUERY = SELECT_RATING_QUERY + " WHERE " + FILTER_BY_WORK;
    String SELECT_RATINGS_BY_USER_ID_QUERY = SELECT_RATING_QUERY + " WHERE " + FILTER_BY_ID;
    String SELECT_USERS_WORK_RATING_QUERY = SELECT_RATING_QUERY + " WHERE " + FILTER_BY_ID + " AND " + FILTER_BY_WORK;

    String SELECT_RATING_BY_ID_QUERY = SELECT_RATING_QUERY + " WHERE r.id = :id";

    @Query(SELECT_RATINGS_BY_WORK_ID_QUERY)
    Slice<RatingResponse> getRatings(Integer workId, Pageable pageable);
    @Query(SELECT_RATINGS_BY_USER_ID_QUERY)
    Slice<RatingResponse> getUserRatings(Integer userId, Pageable pageable);

    @Query(SELECT_USERS_WORK_RATING_QUERY)
    Slice<RatingResponse> getUserWorksRatings(Integer userId, Integer workId, Pageable pageable);

    @Query(SELECT_RATING_BY_ID_QUERY)
    RatingResponse getRating(Integer id);
}
