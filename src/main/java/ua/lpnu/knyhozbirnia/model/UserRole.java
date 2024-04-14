package ua.lpnu.knyhozbirnia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER ("User"), ADMIN ("Admin");

    private final String name;
}
