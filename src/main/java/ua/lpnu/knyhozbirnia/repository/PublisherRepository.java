package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse;
import ua.lpnu.knyhozbirnia.model.Publisher;

import java.util.Optional;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Integer> {
    String SELECT_PUBLISHER_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.publisher.PublisherResponse(p.id, p.name)
        FROM Publisher p
    """;

    String SELECT_PUBLISHER_BY_ID_QUERY = SELECT_PUBLISHER_QUERY + " WHERE p.id = :id";
    String SELECT_PUBLISHER_BY_NAME_QUERY = SELECT_PUBLISHER_QUERY + " WHERE p.name = :name";
    String SELECT_PUBLISHER_LIKE_QUERY = SELECT_PUBLISHER_QUERY + " WHERE p.name ILIKE %:searchTerm%";

    @Query(value = SELECT_PUBLISHER_QUERY)
    Slice<PublisherResponse> findAll(Pageable pageable);

    @Query(value = SELECT_PUBLISHER_BY_ID_QUERY)
    Optional<PublisherResponse> findPublisherById(@Param("id") Integer id);

    @Query(value = SELECT_PUBLISHER_BY_NAME_QUERY)
    Optional<PublisherResponse> findByName(@Param("name") String name);

    @Query(value = SELECT_PUBLISHER_LIKE_QUERY)
    Page<PublisherResponse> findByNameContains(@Param("searchTerm") String searchTerm, Pageable pageable);
}