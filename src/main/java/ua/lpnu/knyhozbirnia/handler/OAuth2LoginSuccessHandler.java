package ua.lpnu.knyhozbirnia.handler;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.people.v1.model.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.lpnu.knyhozbirnia.config.DefaultProperties;
import ua.lpnu.knyhozbirnia.model.User;
import ua.lpnu.knyhozbirnia.model.UserRole;
import ua.lpnu.knyhozbirnia.service.UserService;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.Person;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserService userService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final DefaultProperties defaultProperties;

    @Value("${spring.application.name}")
    private String applicationName;


    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String email = attributes.getOrDefault("email", "").toString();

        userService.getUserByEmail(email)
                .ifPresentOrElse(user -> {
                    DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(user.getRole().name())),
                            attributes, "email");
                    Authentication securityAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(user.getRole().name()), new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                            oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                    SecurityContextHolder.getContext().setAuthentication(securityAuth);
                }, () -> {
                    User user = getUserFromToken(oAuth2AuthenticationToken, attributes);

                    DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(user.getUserRole().name())),
                            attributes, "email");
                    Authentication securityAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(user.getUserRole().name())),
                            oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                    SecurityContextHolder.getContext().setAuthentication(securityAuth);

                    userService.saveUser(user);
                });
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(defaultProperties.getFrontend()+"/oauth2/redirect");
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public User getUserFromToken(OAuth2AuthenticationToken oAuth2AuthenticationToken, Map<String, Object> attributes){
        AccessToken token = new AccessToken(getAccessToken(oAuth2AuthenticationToken), null);
        GoogleCredentials credentials = GoogleCredentials.create(token).createScoped(
                Arrays.asList(PeopleServiceScopes.USERINFO_PROFILE, PeopleServiceScopes.USER_BIRTHDAY_READ, PeopleServiceScopes.USER_GENDER_READ));
        HttpRequestInitializer requestInitializer = httpRequest -> {
            credentials.refreshIfExpired();
            credentials.getRequestMetadata().forEach(httpRequest.getHeaders()::put);
        };
        PeopleService peopleService = new PeopleService.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), requestInitializer)
                .setApplicationName(applicationName)
                .build();

        try {
            Person profile = peopleService.people().get("people/me")
                    .setPersonFields("birthdays,genders")
                    .execute();



            LocalDateTime birthday = Optional.ofNullable(profile.getBirthdays())
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(bday -> bday.getDate() != null)
                    .map(bday -> {
                        Date date = bday.getDate();
                        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay()).atStartOfDay();
                    })
                    .findFirst()
                    .orElse(getRandomBirthday());

            String gender = Optional.ofNullable(profile.getGenders())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(g -> g.getValue().equals("male") ? "m" : g.getValue().equals("female") ? "f" : "n")
                    .filter(g -> !g.equals("n"))
                    .findFirst()
                    .orElse("n");

            Object pfpUrlObj = attributes.getOrDefault("picture", null);
            String pfpUrl = pfpUrlObj == null ? null : pfpUrlObj.toString().replace("s96-c", "s500-c");

            return User
                    .builder()
                    .email(attributes.get("email").toString())
                    .firstName(attributes.getOrDefault("given_name", "_").toString())
                    .lastName(attributes.getOrDefault("family_name", "_").toString())
                    .gender(gender)
                    .birthday(birthday)
                    .pfpUrl(pfpUrl)
                    .userRole(UserRole.USER)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String getAccessToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        return accessToken.getTokenValue();
    }

    private LocalDateTime getRandomBirthday() {
        return getRandomBirthday(LocalDate.of(1970, 1, 1), LocalDate.now().minusYears(18));
    }

    private LocalDateTime getRandomBirthday(LocalDate start, LocalDate end){
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        long randomDays = new Random().nextInt((int) daysBetween);
        return start.plusDays(randomDays).atStartOfDay();
    }

}
