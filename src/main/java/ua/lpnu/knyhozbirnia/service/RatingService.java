package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.rating.RatingRequest;
import ua.lpnu.knyhozbirnia.dto.rating.RatingResponse;
import ua.lpnu.knyhozbirnia.mapper.RatingMapper;
import ua.lpnu.knyhozbirnia.mapper.UserMapper;
import ua.lpnu.knyhozbirnia.model.Rating;
import ua.lpnu.knyhozbirnia.repository.RatingRepository;
import ua.lpnu.knyhozbirnia.repository.WorkRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final WorkRepository workRepository;
    private final AuthService authService;


    public Slice<RatingResponse> getRatings(Integer id, Pageable pageable) {
        return ratingRepository.getRatings(id, pageable);
    }

    public Slice<RatingResponse> getUserRatings(Integer id, Pageable pageable) {
        return ratingRepository
                .getUserRatings(id, pageable)
                .map(rating -> {
                    userMapper.setPfp(rating.user());
                    return rating;}
                );
    }

    public RatingResponse getRating(Integer id) {
        return ratingRepository.getRating(id);
    }

    @Transactional
    @Modifying
    public RatingResponse addRating(RatingRequest ratingRequest) {
        Rating rating = Rating
                .builder()
                .user(userService.getCurrentUser())
                .work(workRepository.findById(ratingRequest.workId()).orElseThrow())
                .score(ratingRequest.score())
                .build();
        return ratingMapper.toResponse(ratingRepository.save(rating));
    }

    @Transactional
    @Modifying
    public RatingResponse editRating(Integer id, RatingRequest ratingRequest) {
        Rating rating = ratingRepository.findById(id).orElseThrow();

        authService.checkEditAuthority(rating.getUser());

        rating.setScore(ratingRequest.score());
        return ratingMapper.toResponse(ratingRepository.save(rating));
    }


    @Transactional
    @Modifying
    public void deleteRating(Integer id) {
        Rating rating = ratingRepository.findById(id).orElseThrow();

        authService.checkEditAuthority(rating.getUser());

        ratingRepository.deleteById(id);
    }
}
