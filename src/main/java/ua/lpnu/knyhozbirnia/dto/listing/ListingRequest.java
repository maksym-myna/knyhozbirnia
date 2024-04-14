package ua.lpnu.knyhozbirnia.dto.listing;

import ua.lpnu.knyhozbirnia.model.ReadingStatus;

public record ListingRequest(Integer workId, ReadingStatus status) {}
