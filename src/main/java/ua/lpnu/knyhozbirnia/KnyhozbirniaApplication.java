package ua.lpnu.knyhozbirnia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import ua.lpnu.knyhozbirnia.config.DefaultProperties;

@SpringBootApplication
@EnableConfigurationProperties(DefaultProperties.class)
@EnableAsync
public class KnyhozbirniaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnyhozbirniaApplication.class, args);
	}

}
