package ua.lpnu.knyhozbirnia.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Component
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler {
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(1);
        cookie.setPath("/");
        cookie.setDomain(request.getServerName());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(),
                    oauthToken.getName()
            );
            if (authorizedClient != null){
                String accessToken = authorizedClient.getAccessToken().getTokenValue();
                revokeGoogleToken(accessToken); // Revoke token before clearing session
            }
        }
        request.getSession().invalidate();
        response.setStatus(HttpServletResponse.SC_OK);
        // redirect to login page or any other page if required
    }

    private void revokeGoogleToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String revokeTokenEndpoint = "https://oauth2.googleapis.com/revoke?token=" + accessToken;
        ResponseEntity<String> response = restTemplate.postForEntity(revokeTokenEndpoint, null, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to revoke token");
        }
    }
}
