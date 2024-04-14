package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse;
import ua.lpnu.knyhozbirnia.model.Subject;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Integer> {
    String SELECT_SUBJECT_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.subject.SubjectResponse(s.id, s.name)
        FROM Subject s
    """;

    String SELECT_SUBJECT_BY_ID_QUERY = SELECT_SUBJECT_QUERY + " WHERE s.id = :id ";
    String SELECT_SUBJECT_BY_NAME_QUERY = SELECT_SUBJECT_QUERY + " WHERE s.name = :name ";
    String SELECT_SUBJECT_LIKE_QUERY = SELECT_SUBJECT_QUERY + " WHERE s.name ILIKE %:searchTerm%";

    @Query(value = SELECT_SUBJECT_QUERY)
    Slice<SubjectResponse> findAll(Pageable pageable);

    @Query(value = SELECT_SUBJECT_BY_ID_QUERY)
    Optional<SubjectResponse> findSubjectById(@Param("id") Integer id);

    @Query(value = SELECT_SUBJECT_BY_NAME_QUERY)
    Optional<SubjectResponse> findByName(@Param("name") String name);

    @Query(value = SELECT_SUBJECT_LIKE_QUERY)
    Page<SubjectResponse> findByNameContains(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Override
    @NonNull
    <S extends Subject> List<S> saveAll(@NonNull Iterable<S> subjects);

    List<Subject> findByNameIn(List<String> names);
}