package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("{id}/ratings/")
    public Slice<RatingResponse> getUsersRatings(
            @PathVariable Integer id,
            @PageableDefault(size = 30) Pageable pageable
    ) {
        return ratingService.getUserRatings(id, pageable);
    }

    @GetMapping("{id}/loans/")
    public Slice<LoanResponse> getUsersLoans(
            @PathVariable Integer id,
            @PageableDefault(size = 30) Pageable pageable
    ) {
        return loanService.getUserLoans(id, pageable);
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
