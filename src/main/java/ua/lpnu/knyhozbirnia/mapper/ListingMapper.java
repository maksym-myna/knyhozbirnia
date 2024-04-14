package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.listing.ListingResponse;
import ua.lpnu.knyhozbirnia.model.Listing;

@Service
public class ListingMapper {
    public ListingResponse toResponse (Listing listing) {
        return new ListingResponse(
                listing.getId(),
                listing.getUser(),
                listing.getWork().getTitle(),
                listing.getWork().getIsbn(),
                listing.getReadingStatus(),
                listing.getListedAt());
    }
}
