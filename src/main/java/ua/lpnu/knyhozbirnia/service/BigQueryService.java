package ua.lpnu.knyhozbirnia.service;

import com.google.cloud.bigquery.*;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.model.bigquery.Attribute;
import ua.lpnu.knyhozbirnia.model.bigquery.Fact;
import ua.lpnu.knyhozbirnia.model.bigquery.Metadata;
import ua.lpnu.knyhozbirnia.model.bigquery.Metric;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class BigQueryService {
    private final Metadata metadata;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final BigQuery bigQuery;

    public ByteArrayInputStream downloadCSV(String tableName, List<String> fields, Integer limit, LocalDate minDate, LocalDate maxDate, Boolean writeHeader) throws IOException, ChangeSetPersister.NotFoundException, InterruptedException {
        String query = createQuery(tableName, fields, limit, minDate.atStartOfDay(), maxDate.atStartOfDay());

        System.out.println(query);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); PrintWriter writer = new PrintWriter(out)) {

            TableResult result = bigQuery.query(queryConfig);

            if (writeHeader){
                writer.println(String.join(",", fields));
            }

            for (FieldValueList row : result.iterateAll()) {
                writer.println(row.stream().map(fieldValue -> {
                    Object value = fieldValue.getValue();
                    return value != null ? value.toString() : "";
                }).collect(Collectors.joining(",")));  }

            writer.flush();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            throw new InterruptedException("BigQuery query was interrupted");
        }
    }

    private String createQuery(String tableName, List<String> fields, Integer limit, LocalDateTime minDate, LocalDateTime maxDate) throws
            ChangeSetPersister.NotFoundException {
        Fact fact = findFact(tableName);
        List<String> allFields = getAllFields(fact).stream().filter(fields::contains).toList();
        Map<String, List<String>> joinedFields = getAllJoinedFields(fact)
                .entrySet().stream()
//                .map(entry -> new AbstractMap.SimpleEntry<>(
//                        entry.getKey(),
//                        entry.getValue().stream().filter(fields::contains).collect(Collectors.toList())))
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String joinClauses = joinedFields.entrySet().stream()
                .sorted(Map.Entry.<String, List<String>>comparingByKey().reversed())
                .filter(entry -> !entry.getKey().equals("work_author") && !entry.getKey().equals("author"))
                .map(entry -> "join " + metadata.getDatasetName() + "." + entry.getKey() + " using(" + entry.getKey()  + "_id)")
                .collect(Collectors.joining(" ")) + " join " + metadata.getDatasetName() + ".work_author using(work_id) join " + metadata.getDatasetName() + ".author using(author_id)";
        List<String> selectedFields = getAllSelectedFields(allFields, joinedFields);
        String selects = getSelects(selectedFields);

//        String joins = getJoins(joinedFields);
        String wheres = getWheres(minDate, maxDate);

        return "SELECT " + String.join(",", fields) + " FROM " + metadata.getDatasetName() + '.' + tableName + " " + joinClauses + " " + wheres + " LIMIT " + limit;
    }

    public Map<String, Map<String, List<String>>> getAllFactsSelectableFields() {
        var map = metadata
                .getFacts()
                .stream()
                .collect(Collectors.toMap(
                        Fact::getTitle,
                        fact -> {
                            try {
                                return getAllSelectableFieldsNoIds(fact.getTitle());
                            } catch (ChangeSetPersister.NotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ));

        for(var entry : map.entrySet()) {
            entry.getValue().values().removeIf(List::isEmpty);
        }
        return map;
    }

    public Map<String, List<String>> getAllSelectableFieldsNoIds(String tableName) throws ChangeSetPersister.NotFoundException {
        Fact fact = findFact(tableName);
        List<String> allFields = getAllFields(fact);
        Map<String, List<String>> joinedFields = getAllJoinedFields(fact);
        joinedFields.put(tableName, allFields);
        joinedFields = joinedFields.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().filter(name -> !name.endsWith("_id") && !name.equals("coefficient")).collect(Collectors.toList())
                ));
        return joinedFields;
    }

    public Map<String, List<String>> getAllSelectableFields(String tableName) throws ChangeSetPersister.NotFoundException {
        Fact fact = findFact(tableName);
        List<String> allFields = getAllFields(fact);
        Map<String, List<String>> joinedFields = getAllJoinedFields(fact);
        joinedFields.put(tableName, allFields);
        return joinedFields;
    }

    private Fact findFact(String factName) throws ChangeSetPersister.NotFoundException {
        return metadata
                .getFacts()
                .stream()
                .filter(f -> f.getTitle().equals(factName))
                .findFirst()
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    private List<String> getAllFields(Fact fact) {
        return Stream
                .concat(fact.getAttributes()
                                .stream()
                                .filter(attribute -> attribute.getForeignKey() == null)
                                .map(Attribute::getTitle),
                        fact.getMetrics().stream()
                                .map(Metric::getTitle)
                ).toList();
    }

    public Map<String, List<String>> getAllJoinedFields(Fact fact){
        Set<String> visited = new HashSet<>();
        Map<String, List<String>> result = fact.getAttributes().stream()
                .filter(attribute -> attribute.getForeignKey() != null)
                .flatMap(attribute -> getRelatedAttributes(attribute.getForeignKey(), visited).entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (list1, list2) -> {
                            list1.addAll(list2); // if there are duplicate keys, merge the lists
                            return list1;
                        }
                ));

        // Iterate over all dimensions in the metadata
        metadata.getDimensions().stream()
                .filter(dimension -> result.containsKey(dimension.getAttributes().stream().map(Attribute::getForeignKey).filter(Objects::nonNull).findAny().orElse(""))
                         && !visited.contains(dimension.getTitle()))
                .forEach(dimension -> result.putAll(getRelatedAttributes(dimension.getTitle(), visited)));

        return result;
    }

    private Map<String, List<String>> getRelatedAttributes(String foreignKey, Set<String> visited) {
        if (visited.contains(foreignKey)) {
            return new HashMap<>();
        }
        visited.add(foreignKey);
        return Stream.concat(metadata.getFacts().stream(), metadata.getDimensions().stream())
                .filter(dimension -> dimension.getTitle().equals(foreignKey))
                .flatMap(dimension -> dimension.getAttributes().stream())
                .flatMap(attribute -> {
                    Map<String, List<String>> titles = new HashMap<>();
                    titles.putIfAbsent(foreignKey, new ArrayList<>());
                    titles.get(foreignKey).add(attribute.getTitle());
                    if (attribute.getForeignKey() != null) {
                        titles.putAll(getRelatedAttributes(attribute.getForeignKey(), visited));
                    }
                    return titles.entrySet().stream();
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }));
    }

    private List<String> getAllSelectedFields(List<String> allFields, Map<String, List<String>> joinedFields) {
        return Stream.concat(
                joinedFields.entrySet().stream().flatMap(entry ->
                        entry.getValue().stream().map(value -> entry.getKey() + "." + value)
                ),
                allFields.stream()
        ).collect(Collectors.toList());
    }

    private String getSelects(List<String> allFields){
        return String.join(", ", allFields);
    }

    private String getJoins(Map<String, List<String>> joinedFields){
        String joins = joinedFields.entrySet().stream()
                .map(entry -> "left join " + metadata.getDatasetName()  + "." + entry.getKey() + " using(" +
                        entry.getValue().stream()
                                .map(x -> entry.getKey().equals("work_author" ) || entry.getKey().equals("author" ) ? "work_id) join " + metadata.getDatasetName() + ".author using(author_id" : entry.getKey() + "_id")
                                .findAny()
                                .orElse("") + ")")
                .collect(Collectors.joining(" "));
        if (!joins.contains("using(date_id)")){
            joins += " left join " + metadata.getDatasetName() + ".date using(date_id)";
        }
        return joins;
    }

    private String getWheres(LocalDateTime minDate, LocalDateTime maxDate){
        return "WHERE date.date BETWEEN '" + minDate.format(formatter) + "' AND '" + maxDate.format(formatter) + "'";
    }
}
