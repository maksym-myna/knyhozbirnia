package ua.lpnu.knyhozbirnia.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ua.lpnu.knyhozbirnia.handler.OAuth2LoginSuccessHandler;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final DefaultProperties defaultProperties;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .headers(headers -> headers.contentTypeOptions(Customizer.withDefaults()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/login/**", "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/authors/**", "/works/**",
                                "/languages/**", "search/**", "publishers/**", "subjects/**", "users/**", "listings/statuses/", "ratings/**", "loans/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "listings/work/{work_id}/", "ratings/work/{work_id}/", "loans/work/{work_id}/", "loans/item/{item_id}/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/authors/**", "/languages/**", "publishers/**", "subjects/**", "works/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/authors/**", "/languages/**", "publishers/**", "subjects/**", "works/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/authors/**", "/languages/**", "publishers/**", "subjects/**", "works/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2Login(oath2 -> {
                    oath2.successHandler(oAuth2LoginSuccessHandler);
                    oath2.failureHandler((_, _, exception) -> {
                        throw new LoginFailedException(exception.getMessage());
                    });
                })
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(defaultProperties.getFrontend()));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }
}

