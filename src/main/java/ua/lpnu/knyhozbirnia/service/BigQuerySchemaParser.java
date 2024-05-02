package ua.lpnu.knyhozbirnia.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.model.bigquery.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BigQuerySchemaParser {
    @Value("${metadata-location}")
    private String metadataLocation;
    private final ObjectMapper objectMapper;

    public String readSchema() throws IOException {
        // Resource location of the JSON schema file (replace with your actual file path)
        ClassLoader classLoader = BigQuerySchemaParser.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(metadataLocation);

        if (inputStream == null) {
            throw new FileNotFoundException("Schema file not found: " + metadataLocation);
        }

        // Read the schema content from the input stream
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public List<Dimension> parseDimensions(JsonNode rootNode) {
        List<Dimension> dimensions = new ArrayList<>();
        JsonNode dimensionsNode = rootNode.get("dimensions");
        for (JsonNode dimensionNode : dimensionsNode) {
            Dimension dimension = new Dimension();
            dimension.setTitle(dimensionNode.get("title").asText());
            List<Attribute> attributes = parseAttributes(dimensionNode.get("attributes"));
            dimension.setAttributes(attributes);
            dimensions.add(dimension);
        }

        return dimensions;
    }

    public List<Fact> parseFacts(JsonNode rootNode) {
        List<Fact> facts = new ArrayList<>();
        JsonNode factsNode = rootNode.get("facts");

        for (JsonNode factNode : factsNode) {
            Fact fact = new Fact();
            fact.setTitle(factNode.get("title").asText());
            fact.setDefinition(factNode.get("definition").asText());
            List<Metric> metrics = parseMetrics(factNode.get("metrics"));
            fact.setMetrics(metrics);
            List<Attribute> attributes = parseAttributes(factNode.get("attributes"));
            fact.setAttributes(attributes);
            facts.add(fact);
        }

        return facts;
    }

    public List<Attribute> parseAttributes(JsonNode attributesNode) {
        List<Attribute> attributes = new ArrayList<>();

        for (JsonNode attributeNode : attributesNode) {
            Attribute attribute = new Attribute();
            attribute.setTitle(attributeNode.get("title").asText());
            attribute.setType(attributeNode.get("type").asText());
            attribute.setPrimaryKey(attributeNode.get("primary_key").asBoolean());

            if (attributeNode.has("subLevel")) {
                ArrayNode subLevelList = objectMapper.createArrayNode();
                subLevelList.add(attributeNode.get("subLevel"));
                List<Attribute> subLevel = parseAttributes(subLevelList);
                attribute.setSubLevel(subLevel.get(0));
            }

            if (attributeNode.has("foreign_key")) {
                attribute.setForeignKey(attributeNode.get("foreign_key").asText());
            }

            attributes.add(attribute);
        }

        return attributes;
    }

    public List<Metric> parseMetrics(JsonNode metricsNode) {
        List<Metric> metrics = new ArrayList<>();

        for (JsonNode metricNode : metricsNode) {
            Metric metric = new Metric();
            metric.setTitle(metricNode.get("title").asText());
            metric.setType(metricNode.get("type").asText());
            metrics.add(metric);
        }

        return metrics;
    }

    public Metadata parseMetadata() throws IOException {
        String jsonString = readSchema();

        // Parse the JSON schema using BigQuerySchemaParser
        JsonNode rootNode = objectMapper.readTree(jsonString);
        List<Dimension> dimensions = parseDimensions(rootNode);
        List<Fact> facts = parseFacts(rootNode);

        // Create and populate the Metadata object
        Metadata metadata = new Metadata();
        metadata.setDatasetName(rootNode.get("dataset_name").asText());  // Set from root node
        metadata.setProjectId(rootNode.get("project_id").asText());   // Set from root node
        metadata.setDimensions(dimensions);
        metadata.setFacts(facts);

        return metadata;
    }
}