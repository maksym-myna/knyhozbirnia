package ua.lpnu.knyhozbirnia.config;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String s) {
        super(s);
    }
}
