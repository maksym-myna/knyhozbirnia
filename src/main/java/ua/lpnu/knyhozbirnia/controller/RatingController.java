package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.rating.RatingRequest;
import ua.lpnu.knyhozbirnia.dto.rating.RatingResponse;
import ua.lpnu.knyhozbirnia.service.RatingService;

@RestController
@RequestMapping("/ratings/")
@AllArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("work/{work_id}/")
    public Slice<RatingResponse> getRatings(
            @PathVariable("work_id") Integer id,
            @PageableDefault(size = 1000) Pageable pageable) {
        return ratingService.getRatings(id, pageable);
    }

    @GetMapping("{id}/")
    public RatingResponse getRating(@PathVariable("id") Integer id) {
        return ratingService.getRating(id);
    }

    @PostMapping
    public RatingResponse addRating(@RequestBody RatingRequest rating){
        return ratingService.addRating(rating);
    }

    @PutMapping("{id}/")
    public RatingResponse editRating(@PathVariable("id") Integer id, @RequestBody RatingRequest rating) {
        return ratingService.editRating(id, rating);
    }

    @DeleteMapping("{id}/")
    public void deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteRating(id);
    }
}
