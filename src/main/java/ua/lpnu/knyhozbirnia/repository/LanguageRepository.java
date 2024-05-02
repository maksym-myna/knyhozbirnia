package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.language.LanguageResponse;
import ua.lpnu.knyhozbirnia.model.Language;

import java.util.Optional;

@Repository
public interface LanguageRepository extends CrudRepository<Language, String> {
    String SELECT_LANGUAGES_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.language.LanguageResponse(l.id, l.name, l.speakers)
        FROM Language l
    """;

    String SELECT_LANGUAGE_BY_ID_QUERY = SELECT_LANGUAGES_QUERY + " WHERE l.id = :id ";
    String SELECT_LANGUAGE_BY_NAME_QUERY = SELECT_LANGUAGES_QUERY + " WHERE l.name = :name ";
    String SELECT_LANGUAGE_LIKE_QUERY = SELECT_LANGUAGES_QUERY + " WHERE l.name ILIKE %:searchTerm%";


    @Query(value = SELECT_LANGUAGES_QUERY)
    Slice<LanguageResponse> findAll(Pageable pageable);

    @Query(value = SELECT_LANGUAGE_BY_ID_QUERY)
    Optional<LanguageResponse> findLanguageById(@Param("id") String id);
    @Query(value = SELECT_LANGUAGE_BY_NAME_QUERY)
    Optional<LanguageResponse> findLanguageByName(@Param("name") String name);

    @Query(value = SELECT_LANGUAGE_LIKE_QUERY)
    Page<LanguageResponse> findByNameContains(@Param("searchTerm") String searchTerm, Pageable pageable);


}
