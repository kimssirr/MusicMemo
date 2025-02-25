package music.memo.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import music.memo.domain.Music;
import music.memo.domain.Review;
import music.memo.domain.User;
import music.memo.dto.ReviewDto;
import music.memo.repository.MusicRepository;
import music.memo.repository.ReviewRepository;
import music.memo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;

    public Map<String, String> validateHandling(BindingResult result) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : result.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    @Transactional(readOnly = false)
    public Long save(ReviewDto dto, Long userId, Long musicId) {
        Review review = dto.toEntity();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        review.setUser(user);
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new EntityNotFoundException("음악을 찾을 수 없습니다."));
        review.setMusic(music);
        return reviewRepository.save(review).getId();
    }

    @Transactional(readOnly = false)
    public ReviewDto updateReview(Long reviewId, ReviewDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        review.setReviewTitle(dto.getReviewTitle());
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());

        return review.toDto();

    }

    public Review getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));
        return review;
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
