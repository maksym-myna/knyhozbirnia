package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.work.PartialWorkResponse;
import ua.lpnu.knyhozbirnia.dto.work.WorkResponse;
import ua.lpnu.knyhozbirnia.dto.work.WorkSearchResponse;
import ua.lpnu.knyhozbirnia.model.Work;

import java.util.Optional;

@Repository
public interface WorkRepository extends CrudRepository<Work, Integer> {
    String SELECT_COPIES_COUNT = "SELECT COUNT(ii.work.id) FROM InventoryItem ii WHERE ii.work.id = w.id";
    String SELECT_AVAILABLE_WORKS_COUNT = """
        SELECT
            COUNT(ii)
        FROM
            InventoryItem ii
        LEFT JOIN
            Loan l on ii.id = l.inventoryItem.id
        WHERE
            l.inventoryItem.work.id = w.id AND (
        (l.loanedAt = (
            SELECT MAX(l2.loanedAt) FROM Loan l2 WHERE l2.inventoryItem.id = l.inventoryItem.id
        ) AND l.returnedAt IS NOT NULL) OR l.loanedAt IS NULL)
    """;
    String SELECT_WORK_QUERY_NO_GROUP_BY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.work.PartialWorkResponse(w.id, w.title, w.pages, w.releaseYear, w.isbn, w.medium, w.weight, w.modifiedAt, p.id, p.name, l.id, l.name,
        cast(STRING_AGG(a.name, ',') as String),
        cast(STRING_AGG(s.name, ',') as String))
        FROM Work w
        JOIN w.language l
        JOIN w.publisher p
        LEFT JOIN w.authors a
        LEFT JOIN w.subjects s
    """;
    String SELECT_WORK_BY_ID_NO_FILTER = """
        SELECT new ua.lpnu.knyhozbirnia.dto.work.WorkResponse(w.id, w.title, w.pages, w.releaseYear, w.isbn, w.medium, w.weight, w.modifiedAt, p.id, p.name, l.id, l.name,
            cast(STRING_AGG(a.name, ',') as String),
            cast(STRING_AGG(s.name, ',') as String),
            (SELECT COUNT(l.work.id) FROM Listing l WHERE l.work.id = w.id AND l.readingStatus = 'CURRENTLY_READING'),
            (SELECT COUNT(l.work.id) FROM Listing l WHERE l.work.id = w.id AND l.readingStatus = 'WANT_TO_READ'),
            (SELECT COUNT(l.work.id) FROM Listing l WHERE l.work.id = w.id AND l.readingStatus = 'ALREADY_READ'),
            (SELECT COALESCE(AVG(r.score),0) FROM Rating r WHERE r.work.id = w.id),
            (SELECT COUNT(r.score) FROM Rating r WHERE r.work.id = w.id),
            (""" + SELECT_COPIES_COUNT + """
            ),
            (""" + SELECT_AVAILABLE_WORKS_COUNT + """
            ))
            FROM Work w
            JOIN w.language l
            JOIN w.publisher p
            LEFT JOIN w.authors a
            LEFT JOIN w.subjects s
        """;

    String SELECT_WORK_GROUP_BY = "GROUP BY w.id, p.id, l.id";
    String FILTER_LANGUAGES_BY_IDS = "(l.id IN (:languageIds))";
    String FILTER_SUBJECTS_BY_NAMES = "(s.name IN (:subjectNames))";
    String FILTER_PUBLISHERS_BY_NAMES = "(p.name IN (:publisherNames))";
    String FILTER_AUTHORS_BY_NAMES = "(a.name IN (:authorNames))";
    String FILTER_MEDIUMS_BY_NAMES = "(w.medium IN (:mediums))";
    String FILTER_MIN_RELEASE_YEAR = "(w.releaseYear >= :minReleaseYear)";
    String FILTER_MAX_RELEASE_YEAR = "(w.releaseYear <= :maxReleaseYear)";
    String FILTER_MIN_WEIGHT = "w.weight >= :minWeight)";
    String FILTER_MAX_WEIGHT = "w.weight <= :maxWeight)";
    String FILTER_MIN_PAGES = "(w.pages >= :minPages)";
    String FILTER_MAX_PAGES = "(w.pages <= :maxPages)";
    String FILTER_AVAILABLE = "(" + SELECT_AVAILABLE_WORKS_COUNT + ") > 0";
    String FILTER_WORKS_WITH_COPIES = "(SELECT COUNT(ii.work.id) FROM InventoryItem ii WHERE ii.work.id = w.id) > 0";
    String SELECT_WORK_QUERY = SELECT_WORK_QUERY_NO_GROUP_BY + ' ' + SELECT_WORK_GROUP_BY;
    String SELECT_WORK_BY_ID_QUERY = SELECT_WORK_BY_ID_NO_FILTER + " WHERE w.id = :id " + SELECT_WORK_GROUP_BY;
    String SELECT_WORK_BY_ISBN_QUERY = SELECT_WORK_BY_ID_NO_FILTER + " WHERE w.isbn = :isbn " + SELECT_WORK_GROUP_BY;
    String WORK_SEARCH_CLAUSE = "WHERE w.title ILIKE %:searchTerm%";
    String SELECT_WORK_LIKE_QUERY = SELECT_WORK_QUERY_NO_GROUP_BY + " " + WORK_SEARCH_CLAUSE + " " + SELECT_WORK_GROUP_BY;
    String SELECT_WORK_TITLES_AND_ID_LIKE_QUERY = "SELECT new ua.lpnu.knyhozbirnia.dto.work.WorkSearchResponse(w.isbn, w.title) FROM Work w " + WORK_SEARCH_CLAUSE;

    @Query(value = SELECT_WORK_QUERY)
    Slice<PartialWorkResponse> findAll(
            Pageable pageable
//            @Param("languageIds") List<Integer> languageIds,
//            @Param("subjectNames") List<String> subjectNames,
//            @Param("publisherNames") List<String> publisherNames,
//            @Param("authorNames") List<String> authorNames,
//            @Param("mediums") List<WorkMedium> mediums,
//            @Param("minReleaseYear") Integer minReleaseYear,
//            @Param("maxReleaseYear") Integer maxReleaseYear,
//            @Param("minWeight") Float minWeight,
//            @Param("maxWeight") Float maxWeight,
//            @Param("minPages") Integer minPages,
//            @Param("maxPages") Integer maxPages
    );
    @Query(value = SELECT_WORK_BY_ID_QUERY)
    Optional<WorkResponse> findWorkById(@Param("id") Integer id);
    @Query(value = SELECT_WORK_BY_ISBN_QUERY)
    Optional<WorkResponse> findWorkByIsbn(@Param("isbn") String isbn);
    @Query(value = SELECT_WORK_LIKE_QUERY)
    Slice<PartialWorkResponse> findByTitleContains(@Param("searchTerm") String searchTerm, Pageable pageable);
    @Query(value = SELECT_WORK_TITLES_AND_ID_LIKE_QUERY)
    Slice<WorkSearchResponse> findTitlesAndIdsByTitleContains(@Param("searchTerm") String searchTerm, Pageable pageable);

    void deleteByIsbn(String isbn);
}