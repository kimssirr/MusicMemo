package music.memo.dto;

import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import music.memo.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5~20자 사이여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,16}$",
            message = "비밀번호는 8~16자이며, 영문 대 소문자, 숫자, 특수문자를 사용하세요."
    )
    private String password;


    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 10)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    public User toEntity() {
        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();

        return user;
    }
}