package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.listing.GroupedListingResponse;
import ua.lpnu.knyhozbirnia.dto.loan.LoanResponse;
import ua.lpnu.knyhozbirnia.dto.rating.RatingResponse;
import ua.lpnu.knyhozbirnia.dto.user.UserRequest;
import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.model.UserRole;
import ua.lpnu.knyhozbirnia.service.ListingService;
import ua.lpnu.knyhozbirnia.service.LoanService;
import ua.lpnu.knyhozbirnia.service.RatingService;
import ua.lpnu.knyhozbirnia.service.UserService;

@RestController
@RequestMapping("/users/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final LoanService loanService;
    private final ListingService listingService;
    private final RatingService ratingService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("roles/")
    public Slice<UserRole> getRoles() {
        return userService.getRoles();
    }

    @GetMapping
    public UserResponse getCurrentUser() {
        return userService.getCurrentUserResponse();
    }

    @GetMapping("{id}/")
    public UserResponse getUser(@PathVariable Integer id) {
        return userService.getUserById(id).orElseThrow();
    }

    @GetMapping("{id}/listings/")
    public GroupedListingResponse getUsersListings(
            @PathVariable Integer id,
            @PageableDefault(size = 30) Pageable pageable
    ) {
        return listingService.getUserListings(id, pageable);
    }

    @GetMapping("listings/")
    public GroupedListingResponse getUsersListings(@PageableDefault(size = 30) Pageable pageable) {
        return listingService.getUserListings(pageable);
    }

    @GetMapping("listings/{workId}/")
    public GroupedListingResponse getUsersWorkListings(@PathVariable  Integer workId, @PageableDefault(size = 30) Pageable pageable) {
        return listingService.getUserWorksListings(workId, pageable);
    }

    @GetMapping("{userId}/listings/{workId}/")
    public GroupedListingResponse getUsersWorkListings(@PathVariable  Integer userId, @PathVariable  Integer workId, @PageableDefault(size = 30) Pageable pageable) {
        return listingService.getUserWorksListings(userId, workId, pageable);
    }
    @GetMapping("{id}/ratings/")
    public Slice<RatingResponse> getUsersRatings(
            @PathVariable Integer id,
            @PageableDefault(size = 30) Pageable pageable
    ) {
        return ratingService.getUserRatings(id, pageable);
    }

    @GetMapping("ratings/")
    public Slice<RatingResponse> getUsersRatings(@PageableDefault(size = 30) Pageable pageable) {
        return ratingService.getUserRatings(pageable);
    }

    @GetMapping("{id}/loans/")
    public Slice<LoanResponse> getUsersLoans(
            @PathVariable Integer id,
            @PageableDefault(size = 30) Pageable pageable
    ) {
        return loanService.getUserLoans(id, pageable);
    }

    @GetMapping("loans/")
    public Slice<LoanResponse> getUsersLoans(@PageableDefault(size = 30) Pageable pageable) {
        return loanService.getUserLoans(pageable);
    }

    @GetMapping("loans/unreturned/")
    public Slice<LoanResponse> getUsersUnreturnedLoans(@PageableDefault(size = 15) Pageable pageable) {
        return loanService.getUserUnreturnedLoans(pageable);
    }

    @GetMapping("{id}/loans/unreturned/")
    public Slice<LoanResponse> getUsersUnreturnedLoans(
            @PathVariable Integer id,
            @PageableDefault(size = 15) Pageable pageable
    ) {
        return loanService.getUserUnreturnedLoans(id, pageable);
    }

    @GetMapping("{id}/loans/returned/")
    public Slice<LoanResponse> getUsersReturnedLoans(
            @PathVariable Integer id,
            @PageableDefault(size = 15) Pageable pageable
    ) {
        return loanService.getUserReturnedLoans(id, pageable);
    }

    @GetMapping("loans/returned/")
    public Slice<LoanResponse> getUsersReturnedLoans(@PageableDefault(size = 15) Pageable pageable) {
        return loanService.getUserReturnedLoans(pageable);
    }


    @GetMapping("email/{email}/")
    public UserResponse getUser(@PathVariable String email) {
        return userService.getUserByEmail(email).orElseThrow();
    }


    @PutMapping("{id}/")
    public UserResponse updateUser(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        return userService.updateUser(userRequest, id);
    }

    @DeleteMapping("{id}/")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
    }

}
