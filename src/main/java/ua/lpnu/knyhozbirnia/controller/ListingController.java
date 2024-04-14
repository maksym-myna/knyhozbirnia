package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.listing.ListingRequest;
import ua.lpnu.knyhozbirnia.dto.listing.ListingResponse;
import ua.lpnu.knyhozbirnia.dto.listing.GroupedListingResponse;
import ua.lpnu.knyhozbirnia.model.ReadingStatus;
import ua.lpnu.knyhozbirnia.model.WorkMedium;
import ua.lpnu.knyhozbirnia.service.ListingService;

import java.util.List;

@RestController
@RequestMapping("/listings/")
@AllArgsConstructor
public class ListingController {
    private ListingService listingService;

    @GetMapping("work/{work_id}/")
    public GroupedListingResponse getListings(
            @PathVariable("work_id") Integer id,
            @RequestParam(required = false) List<ReadingStatus> status,
            @PageableDefault(size = 1000) Pageable pageable) {
        return listingService.getListings(id, pageable);
    }

    @GetMapping("{id}/")
    public ListingResponse getListing(@PathVariable("id") Integer id) {
        return listingService.getListing(id);
    }

    @GetMapping("statuses/")
    public Slice<ReadingStatus> getReadingStatuses() {
        return listingService.getReadingStatuses();
    }

    @PostMapping
    public ListingResponse addListing(@RequestBody ListingRequest listing) {
        return listingService.addListing(listing);
    }

    @DeleteMapping("{id}/")
    public void deleteListing(@PathVariable("id") Integer id) {
        listingService.deleteListing(id);
    }
}
