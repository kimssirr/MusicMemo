package music.memo.Service;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import music.memo.dto.ReviewDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    private Validator validator;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 리뷰_유효성_검사() {
        // Given: 제목 X, 내용 X, 평점 0
        ReviewDto reviewDto = new ReviewDto("", "", 0);

        // When
        Set<ConstraintViolation<ReviewDto>> violations = validator.validate(reviewDto);
        BindingResult bindingResult = new BeanPropertyBindingResult(reviewDto, "reviewDto");

        for (ConstraintViolation<ReviewDto> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), "valid_" + violation.getPropertyPath(), violation.getMessage());
        }

        Map<String, String> errors = reviewService.validateHandling(bindingResult);

        // Then
        assertTrue(errors.containsKey("valid_reviewTitle"), "valid_reviewTitle 키가 존재하지 않습니다.");
        assertEquals("제목은 필수 입력 값입니다.", errors.get("valid_reviewTitle"));

        assertTrue(errors.containsKey("valid_content"), "valid_content 키가 존재하지 않습니다.");
        assertEquals("내용은 필수 입력 값입니다.", errors.get("valid_content"));

        assertTrue(errors.containsKey("valid_rating"), "valid_rating 키가 존재하지 않습니다.");
        assertEquals("평점은 최소 1점 이상이어야 합니다.", errors.get("valid_rating"));
    }
}