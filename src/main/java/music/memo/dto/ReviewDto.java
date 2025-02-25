package music.memo.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import music.memo.domain.Review;

@Data
@NoArgsConstructor
public class ReviewDto {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 20, message = "제목은 최대 20자까지 입력 가능합니다.")
    private String reviewTitle;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 200, message = "내용은 최대 200자까지 입력 가능합니다.")
    private String content;

    @Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점까지 가능합니다.")
    private Integer rating;

    @Builder
    public ReviewDto(String reviewTitle, String content, Integer rating) {
        this.reviewTitle = reviewTitle;
        this.content = content;
        this.rating = rating;
    }

    public Review toEntity() {
        return Review.builder()
                .reviewTitle(reviewTitle)
                .content(content)
                .rating(rating)
                .build();
    }
}
