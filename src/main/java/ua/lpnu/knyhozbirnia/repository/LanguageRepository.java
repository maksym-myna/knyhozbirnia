package ua.lpnu.knyhozbirnia.repository;

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
    String SELECT_LANGUAGE_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.language.LanguageResponse(l.id, l.name, l.speakers)
        FROM Language l
    """;

    String SELECT_LANGUAGE_BY_ID_QUERY = SELECT_LANGUAGE_QUERY + " WHERE l.id = :id ";

    @Query(value = SELECT_LANGUAGE_QUERY)
    Slice<LanguageResponse> findAll(Pageable pageable);

    @Query(value = SELECT_LANGUAGE_BY_ID_QUERY)
    Optional<LanguageResponse> findLanguageById(@Param("id") String id);
}
