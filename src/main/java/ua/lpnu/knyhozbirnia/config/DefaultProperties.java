package ua.lpnu.knyhozbirnia.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "default.url")
@Data
public class DefaultProperties {
    private Map<String, String> pfp;
    private String frontend;
}
