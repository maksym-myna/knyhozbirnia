package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.contstants.RuntimeExceptionMessages;
import ua.lpnu.knyhozbirnia.model.User;

@Service
@AllArgsConstructor
public class AuthService {
    public void checkEditAuthority(User user){
        if(!isActionAuthorized(user)){
            throw new AuthorizationServiceException(RuntimeExceptionMessages.AUTHORIZATION_EXCEPTION_MESSAGE);
        }
    }

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    private boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.isAuthenticated() && auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("Admin"));
    }

    public boolean isActionAuthorized(User desiredUser) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return isAdmin() || (auth.isAuthenticated() && auth.getName().equals(desiredUser.getEmail()));
    }
}
