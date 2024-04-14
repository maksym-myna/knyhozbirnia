package ua.lpnu.knyhozbirnia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkMedium {
    EBOOK("E-book"), BOOK("Book"), AUDIOBOOK("Audiobook");

    private final String name;
}