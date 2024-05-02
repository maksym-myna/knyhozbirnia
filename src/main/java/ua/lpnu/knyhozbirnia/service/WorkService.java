package ua.lpnu.knyhozbirnia.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.contstants.RuntimeExceptionMessages;
import ua.lpnu.knyhozbirnia.dto.author.AuthorRequest;
import ua.lpnu.knyhozbirnia.dto.subject.SubjectRequest;
import ua.lpnu.knyhozbirnia.dto.work.PartialWorkResponse;
import ua.lpnu.knyhozbirnia.dto.work.WorkRequest;
import ua.lpnu.knyhozbirnia.dto.work.WorkResponse;
import ua.lpnu.knyhozbirnia.mapper.PublisherMapper;
import ua.lpnu.knyhozbirnia.mapper.WorkMapper;
import ua.lpnu.knyhozbirnia.model.*;
import ua.lpnu.knyhozbirnia.repository.WorkRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final WorkMapper workMapper;

    private final PublisherService publisherService;
    private final PublisherMapper publisherMapper;
    private final SubjectService subjectService;
    private final AuthorService authorService;
    private final InventoryItemService itemService;
    private final UserService userService;

    private final EntityManager entityManager;
    public Slice<PartialWorkResponse> getAllWorks(
            Pageable pageable,
            List<String> languages,
            List<String> subjectNames,
            List<String> publisherNames,
            List<String> authorNames,
            List<String> listingTypes,
            List<Integer> ratings,
            List<String> mediums,
            Integer minReleaseYear,
            Integer maxReleaseYear,
            Float minWeight,
            Float maxWeight,
            Integer minPages,
            Integer maxPages,
            Boolean isAvailable,
            Boolean isAccounted,
            Integer userId
    ) {
        var query = buildSelectionQuery(
                languages,
                subjectNames,
                publisherNames,
                authorNames,
                listingTypes,
                ratings,
                mediums,
                minReleaseYear,
                maxReleaseYear,
                minWeight,
                maxWeight,
                minPages,
                maxPages,
                isAvailable,
                isAccounted,
                userId
        );

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize + 1);

        List<PartialWorkResponse> results = query.getResultList();
        boolean hasMore = results.size() > pageSize;

        if (hasMore) {
            results = results.subList(0, pageSize);
        }

        return new SliceImpl<>(results, pageable, hasMore);
    }

    public WorkResponse getWork(Integer id) {
        return workRepository.findWorkById(id).orElseThrow();
    }
    public WorkResponse getWorkByIsbn(String isbn) {
        return workRepository.findWorkByIsbn(isbn).orElseThrow();
    }
    public Slice<WorkMedium> getMediums() {
        return new SliceImpl<>(List.of(WorkMedium.values()));
    }

    @Transactional
    @Modifying
    public WorkResponse addWork(WorkRequest workRequest) {
        return upsertWork(workRequest, null);
    }

    @Transactional
    @Modifying
    public WorkResponse upsertWork(WorkRequest workRequest, Integer id) {
        Work mappedWork = workMapper.toEntity(workRequest, id);
        if (mappedWork.getLanguage() == null) {
            throw new ConstraintViolationException(RuntimeExceptionMessages.LANGUAGE_IS_REQUIRED_EXCEPTION_MESSAGE, null);
        }

        if (mappedWork.getPublisher() == null) {
            var publisher = publisherMapper.toEntity(publisherService.addPublisher(workRequest.publisher()));
            mappedWork.setPublisher(publisher);
        }
        System.out.println(1);
        addEntities(mappedWork.getSubjects(), workRequest.subjects(), Subject::getName, SubjectRequest::name, subjectService::addSubjects);
        addEntities(mappedWork.getAuthors(), workRequest.authors(), Author::getName, AuthorRequest::name, authorService::addAuthors);

        Work savedWork = workRepository.save(mappedWork);

        itemService.addItems(savedWork, workRequest.quantity());

        return workMapper.toResponse(savedWork);
    }

    @Transactional
    @Modifying
    public void deleteWorkById(Integer id) {
        workRepository.deleteById(id);
    }

    @Transactional
    @Modifying
    public void deleteWorkByIsbn(String isbn) {
        workRepository.deleteByIsbn(isbn);
    }

    private <T, R> void addEntities(List<T> mappedEntities, List<R> requestEntities, Function<T, String> getMappedName, Function<R, String> getRequestName, Function<List<R>, List<T>> addEntities) {
        if (mappedEntities.size() < requestEntities.size()) {
            List<String> mappedNames = mappedEntities.stream().map(getMappedName).toList();
            List<R> filteredEntities = requestEntities.stream()
                    .filter(java.util.function.Predicate.not(entity -> mappedNames.contains(getRequestName.apply(entity))))
                    .toList();
            var entities = addEntities.apply(filteredEntities);
            mappedEntities.addAll(entities);
        }
    }

    private Query buildSelectionQuery(
            List<String> languages,
            List<String> subjectNames,
            List<String> publisherNames,
            List<String> authorNames,
            List<String> listingTypes,
            List<Integer> ratings,
            List<String> mediums,
            Integer minReleaseYear,
            Integer maxReleaseYear,
            Float minWeight,
            Float maxWeight,
            Integer minPages,
            Integer maxPages,
            Boolean isAvailable,
            Boolean isAccounted,
            Integer userId
    ){
        int MAX_PARAMS = 11;
        int MAX_QUERY_LENGTH = 1100;

        List<String> clauses = new ArrayList<>(MAX_PARAMS);
        Map<String, Object> params = new LinkedHashMap<>(MAX_PARAMS);

        String selectBase;
        String userClause = null;
        if (ratings != null) {
            selectBase = workRepository.SELECT_WORK_RATINGS_QUERY_NO_GROUP_BY;
            userClause = workRepository.FILTER_RATING_USER;
        } else {
            selectBase = workRepository.SELECT_WORK_BASIC_QUERY_NO_GROUP_BY;
        }

        if (listingTypes != null) {
            if (ratings != null) {
                selectBase = workRepository.SELECT_WORK_FULL_QUERY_NO_GROUP_BY;
                userClause = workRepository.FILTER_USER;
            } else {
                selectBase = workRepository.SELECT_WORK_LISTINGS_QUERY_NO_GROUP_BY;
                userClause = workRepository.FILTER_LISTING_USER;
            }
            clauses.add(workRepository.FILTER_LISTINGS + listingTypes.stream().map(medium -> "'" + medium + "'").collect(Collectors.joining(", ")) + ')');
        }

        if (mediums != null) {
            clauses.add(workRepository.FILTER_MEDIUMS_BY_NAMES + mediums.stream().map(medium -> "'" + medium + "'").collect(Collectors.joining(", ")) + ')');
        }

        System.out.println(selectBase);
        System.out.println(userClause);
        System.out.println(clauses);
        if (userClause != null) {
            if (userId==null){
                userId = userService.getCurrentUser().getId();
            }
            addClauseAndParam(clauses, params, userClause, "userId", userId);
        }
        addClauseAndParam(clauses, params, workRepository.FILTER_RATINGS, "ratings", ratings);
        System.out.println(ratings);
        addClauseAndParam(clauses, params, workRepository.FILTER_MIN_RELEASE_YEAR, "minReleaseYear", minReleaseYear);
        addClauseAndParam(clauses, params, workRepository.FILTER_MAX_RELEASE_YEAR, "maxReleaseYear", maxReleaseYear);
        addClauseAndParam(clauses, params, workRepository.FILTER_MIN_PAGES, "minPages", minPages);
        addClauseAndParam(clauses, params, workRepository.FILTER_MAX_PAGES, "maxPages", maxPages);
        addClauseAndParam(clauses, params, workRepository.FILTER_MIN_WEIGHT, "minWeight", minWeight);
        addClauseAndParam(clauses, params, workRepository.FILTER_MAX_WEIGHT, "maxWeight", maxWeight);
        addClauseAndParam(clauses, params, workRepository.FILTER_SUBJECTS_BY_NAMES, "subjectNames", subjectNames);
        addClauseAndParam(clauses, params, workRepository.FILTER_LANGUAGES_BY_IDS, "languages", languages);
        addClauseAndParam(clauses, params, workRepository.FILTER_PUBLISHERS_BY_NAMES, "publisherNames", publisherNames);
        addClauseAndParam(clauses, params, workRepository.FILTER_AUTHORS_BY_NAMES, "authorNames", authorNames);

        String having = isAvailable
                ? " HAVING " + workRepository.FILTER_AVAILABLE
                : isAccounted ? " HAVING " + workRepository.FILTER_WORKS_WITH_COPIES
                : "";

        StringBuilder queryBuilder = new StringBuilder(MAX_QUERY_LENGTH);
        queryBuilder.append(selectBase);
        if (!clauses.isEmpty()){
            queryBuilder.append(" WHERE ");
            queryBuilder.append(String.join(" AND ", clauses));
        }
        String query = queryBuilder.append(workRepository.SELECT_WORK_GROUP_BY).append(having).toString();

        TypedQuery<PartialWorkResponse> compiledQuery = entityManager.createQuery(query, PartialWorkResponse.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            compiledQuery.setParameter(entry.getKey(), entry.getValue());
        }
        return compiledQuery;
    }

    private void addClauseAndParam(List<String> clauses, Map<String, Object> params, String clause, String paramName, Object paramValue) {
        if (paramValue != null) {
            clauses.add(clause);
            params.put(paramName, paramValue);
        }
    }
}
