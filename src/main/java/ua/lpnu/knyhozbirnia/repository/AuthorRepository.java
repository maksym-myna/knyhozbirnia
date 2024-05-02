package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.author.AuthorResponse;
import ua.lpnu.knyhozbirnia.dto.author.AuthorWorkResponse;
import ua.lpnu.knyhozbirnia.model.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
    String SELECT_AUTHOR_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.author.AuthorResponse(a.id, a.name)
        FROM Author a
    """;

    String SELECT_AUTHORS_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.author.AuthorWorkResponse(a.name,
            (SELECT w.isbn FROM Work w JOIN w.authors a1 WHERE a1.name = a.name ORDER BY w.isbn ASC LIMIT 1))
            FROM AuthorWorkCount a
    """;

    String SELECT_AUTHOR_BY_ID_QUERY = SELECT_AUTHOR_QUERY + " WHERE a.id = :id";
    String SELECT_AUTHOR_BY_NAME_QUERY = SELECT_AUTHOR_QUERY + " WHERE a.name = :name";
    String SELECT_AUTHOR_LIKE_QUERY = SELECT_AUTHOR_QUERY + " WHERE a.name ILIKE %:searchTerm%";

    @Query(SELECT_AUTHORS_QUERY)
    Slice<AuthorWorkResponse> findAll(Pageable pageable);

    @Query(SELECT_AUTHOR_BY_ID_QUERY)
    Optional<AuthorResponse> findAuthorById(@Param("id") Integer id);

    @Query(SELECT_AUTHOR_BY_NAME_QUERY)
    Optional<AuthorResponse> findByName(@Param("name") String name);

    List<Author> findByNameIn(List<String> names);

    @Override
    @NonNull
    <S extends Author> List<S> saveAll(@NonNull Iterable<S> authors);

    @Query(SELECT_AUTHOR_LIKE_QUERY)
    Slice<AuthorResponse> findByNameContains(@Param("searchTerm") String searchTerm, Pageable pageable);

}