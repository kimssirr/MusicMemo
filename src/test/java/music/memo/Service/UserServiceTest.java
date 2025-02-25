package music.memo.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import music.memo.dto.UserSignUpDto;
import music.memo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private Validator validator;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 회원가입_아이디_유효성() {
        // Given: 아이디가 5자 미만
        UserSignUpDto userDto = new UserSignUpDto("abc", "Password123!", "nickname", "test@email.com");

        // When
        Set<ConstraintViolation<UserSignUpDto>> violations = validator.validate(userDto);
        BindingResult bindingResult = new BeanPropertyBindingResult(userDto, "userSignUpDto");

        for (ConstraintViolation<UserSignUpDto> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), "valid_" + violation.getPropertyPath(), violation.getMessage());
        }

        Map<String, String> errors = userService.validateHandling(bindingResult);

        // Then
        assertTrue(errors.containsKey("valid_username"), "valid_username 키가 존재하지 않습니다.");
        assertEquals("아이디는 5~20자 사이여야 합니다.", errors.get("valid_username"));
    }

    @Test
    public void 회원가입_중복_검사() throws Exception {
        // Given
        UserSignUpDto dto = new UserSignUpDto("duplicateUser", "Password1!", "uniqueNick", "duplicate@email.com");

        // Mocking: 중복
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(true);
        when(userRepository.existsByNickname(dto.getNickname())).thenReturn(true);
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // When
        Map<String, String> errors = userService.checkDuplication(dto);

        // Then
        assertFalse(errors.isEmpty()); // 중복된 필드가 있어야 함
        System.out.println("Errors: " + errors);
        assertEquals("이미 존재하는 아이디입니다.", errors.get("Duplication_username"));
        assertEquals("이미 존재하는 이메일입니다.", errors.get("Duplication_email"));

        // Verify: mock이 호출되었는지 확인
        verify(userRepository, times(1)).existsByUsername(dto.getUsername());
        verify(userRepository, times(1)).existsByNickname(dto.getNickname());
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
    }


    static Stream<Arguments> invalidPasswords() {
        String errorMessage = "비밀번호는 8~16자이며, 영문 대 소문자, 숫자, 특수문자를 사용하세요.";

        return Stream.of(
                Arguments.of("Abc1@", errorMessage),  // 8자 미만
                Arguments.of("VeryLongPassword123!", errorMessage), // 16자 초과
                Arguments.of("NoNumber!", errorMessage), // 숫자 없음
                Arguments.of("NoSpecial123", errorMessage), // 특수문자 없음
                Arguments.of("FLOWER123!", errorMessage), // 소문자 없음
                Arguments.of("flower123!", errorMessage) // 대문자 없음
        );
    }

    @DisplayName("비밀번호 유효성 검사 테스트")
    @ParameterizedTest
    @MethodSource("invalidPasswords")
    public void 회원가입_비밀번호_유효성(String password, String expectedMessage) {
        // Given
        UserSignUpDto userDto = new UserSignUpDto("validUser", password, "nickname", "test@email.com");

        // When
        Set<ConstraintViolation<UserSignUpDto>> violations = validator.validate(userDto);
        BindingResult bindingResult = new BeanPropertyBindingResult(userDto, "userSignUpDto");

        for (ConstraintViolation<UserSignUpDto> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            bindingResult.rejectValue(fieldName, "valid_" + fieldName, violation.getMessage());
        }

        Map<String, String> errors = userService.validateHandling(bindingResult);

        // Then
        assertTrue(errors.containsKey("valid_password"), "valid_password 키가 존재하지 않습니다.");
        assertEquals(expectedMessage.trim(), errors.get("valid_password").trim());
    }


}