package ua.lpnu.knyhozbirnia.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.lpnu.knyhozbirnia.model.bigquery.Metadata;
import ua.lpnu.knyhozbirnia.service.BigQuerySchemaParser;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class BigQueryConfig {

    private final BigQuerySchemaParser bigQuerySchemaParser;

    @Bean
    public Metadata bigQuerySchema() throws IOException {
        return bigQuerySchemaParser.parseMetadata();
    }

    @Bean
    public BigQuery bigQuery() {
        return BigQueryOptions.getDefaultInstance().getService();
    }
}
