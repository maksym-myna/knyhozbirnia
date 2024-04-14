package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.listing.ListingRequest;
import ua.lpnu.knyhozbirnia.dto.listing.ListingResponse;
import ua.lpnu.knyhozbirnia.dto.listing.GroupedListingResponse;
import ua.lpnu.knyhozbirnia.mapper.ListingMapper;
import ua.lpnu.knyhozbirnia.mapper.UserMapper;
import ua.lpnu.knyhozbirnia.model.Listing;
import ua.lpnu.knyhozbirnia.model.ReadingStatus;
import ua.lpnu.knyhozbirnia.repository.ListingRepository;
import ua.lpnu.knyhozbirnia.repository.WorkRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final WorkRepository workRepository;
    private final AuthService authService;

    public GroupedListingResponse getListings(Integer id, Pageable pageable) {
        var listings = listingRepository.getListings(id, pageable);
        return new GroupedListingResponse(listings);
    }

    public GroupedListingResponse getUserListings(Integer id, Pageable pageable) {
        var listings = listingRepository.getUserListings(id, pageable).map(listing -> {
            userMapper.setPfp(listing.user());
            return listing;
        });
        return new GroupedListingResponse(listings);
    }

    public ListingResponse getListing(Integer id) {
        return listingRepository.getListing(id);
    }

    public Slice<ReadingStatus> getReadingStatuses() {
        return new SliceImpl<>(List.of(ReadingStatus.values()));
    }

    @Transactional
    @Modifying
    public ListingResponse addListing(ListingRequest listingRequest) {
        Listing listing = Listing
                .builder()
                .user(userService.getCurrentUser())
                .work(workRepository.findById(listingRequest.workId()).orElseThrow())
                .readingStatus(listingRequest.status())
                .build();
        return listingMapper.toResponse(listingRepository.save(listing));
    }

    @Transactional
    @Modifying
    public void deleteListing(Integer id) {
        Listing listing = listingRepository.findById(id).orElseThrow();

        authService.checkEditAuthority(listing.getUser());

        listingRepository.deleteById(id);
    }
}
