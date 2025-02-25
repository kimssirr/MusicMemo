package music.memo.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import music.memo.Service.CustomUserDetailService;
import music.memo.Service.MusicService;
import music.memo.Service.ReviewService;
import music.memo.domain.Music;
import music.memo.domain.Review;
import music.memo.domain.User;
import music.memo.dto.ReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {
    private final MusicService musicService;
    private final CustomUserDetailService userService;
    private final ReviewService reviewService;

    /**
    리뷰 작성
     */
    @GetMapping("/write/{userId}/{musicId}")
    public String showReviewForm(@PathVariable Long userId, @PathVariable Long musicId, Model model) {
        User user = userService.loadUserById(userId);
        Music music = musicService.findMusicByMusicID(musicId);

        model.addAttribute("reviewDto",new ReviewDto());

        model.addAttribute("user", user.getId());
        model.addAttribute("musicId", music.getId());
        model.addAttribute("musicTitle", music.getMusicTitle());
        model.addAttribute("musicArtist", music.getArtist());

        return "private/review/reviewForm";
    }

    @PostMapping("/write/{userId}/{musicId}")
    public String writeReview(@PathVariable Long userId,
                             @PathVariable Long musicId,
                             @Valid @ModelAttribute("reviewDto") ReviewDto dto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            Map<String, String> validatorResult = reviewService.validateHandling(result);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            Music music = musicService.findMusicByMusicID(musicId);
            model.addAttribute("userId", userId);
            model.addAttribute("musicId", musicId);
            model.addAttribute("musicTitle", music.getMusicTitle());
            model.addAttribute("musicArtist", music.getArtist());

            model.addAttribute("reviewDto",dto);
            return "private/review/reviewForm"; // 유효성 검사 실패 시 다시 폼으로
        }

        reviewService.save(dto, userId, musicId);
        return "redirect:/music/list"; // 리뷰 저장 후 음악 목록으로 이동
    }

    /**
     리뷰 상세 조회 페이지
     */
    @GetMapping("/view/{reviewId}")
    public String viewReview(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.getReviewById(reviewId);
        model.addAttribute("reviewView",review);

        model.addAttribute("musicTitle", review.getMusic().getMusicTitle());
        model.addAttribute("musicArtist", review.getMusic().getArtist());
        return "private/review/reviewOneView"; // 리뷰 상세 보기 페이지
    }

    /**
     리뷰 수정 폼 이동
     */
    @GetMapping("/edit/{reviewId}")
    public String editReviewForm(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.getReviewById(reviewId);
        model.addAttribute("musicTitle", review.getMusic().getMusicTitle());
        model.addAttribute("musicArtist", review.getMusic().getArtist());
        model.addAttribute("reviewEdit",review);
        return "private/review/reviewEditForm"; // 수정 폼 페이지
    }

    /**
     리뷰 수정 요청
     */
    @PostMapping("/edit/{reviewId}")
    public String updateReview(@PathVariable Long reviewId,
                               @Valid @ModelAttribute ReviewDto reviewDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Map<String, String> validatorResult = reviewService.validateHandling(result);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            model.addAttribute("reviewEdit",reviewDto);
            return "private/review/reviewEditForm";
        }
        reviewService.updateReview(reviewId, reviewDto);
        return "redirect:/review/view/" + reviewId;
    }

    /**
     리뷰 삭제 요청
     */
    @PostMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/music/list"; // 삭제 후 목록 페이지로 이동
    }

}
