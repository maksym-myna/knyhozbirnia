package ua.lpnu.knyhozbirnia.dto.listing;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ua.lpnu.knyhozbirnia.model.ReadingStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record GroupedListingResponse(
        List<ListingResponse> wantToRead,
        List<ListingResponse> reading,
        List<ListingResponse> alreadyRead,
        int numberOfElements,
        boolean hasNext,
        boolean hasPrevious,
        Pageable pageable
) {
    public GroupedListingResponse(Slice<ListingResponse> listings) {
        this(listings.stream().collect(Collectors.groupingBy(ListingResponse::status)), listings);
    }
    public GroupedListingResponse(
            Map<ReadingStatus, List<ListingResponse>> groupedListings,
            Slice<ListingResponse> listings
    ) {
        this(
                groupedListings.get(ReadingStatus.WANT_TO_READ),
                groupedListings.get(ReadingStatus.CURRENTLY_READING),
                groupedListings.get(ReadingStatus.ALREADY_READ),
                listings.getNumberOfElements(),
                listings.hasNext(),
                listings.hasPrevious(),
                listings.getPageable()
        );
    }
}
