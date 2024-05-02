package ua.lpnu.knyhozbirnia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkMedium {
    EBOOK("EBOOK"), BOOK("BOOK"), AUDIOBOOK("AUDIOBOOK");

    private final String name;
}