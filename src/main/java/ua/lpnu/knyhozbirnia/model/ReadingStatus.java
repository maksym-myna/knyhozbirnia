package ua.lpnu.knyhozbirnia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReadingStatus {
    WANT_TO_READ("Want to read"), CURRENTLY_READING("Currently reading"), ALREADY_READ("Already read");

    private final String name;
}
