package music.memo;

import music.memo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testSignUpSuccess() throws Exception {
        // Given: 회원가입 폼 데이터
        mockMvc.perform(post("/public/signup")
                        .param("username", "testuser")
                        .param("password", "Password1!")
                        .param("nickname", "테스트닉")
                        .param("email", "testuser@email.com")
                        .with(csrf())) // CSRF 토큰 추가 (Spring Security 적용 시 필요)
                .andExpect(status().is3xxRedirection()) // 회원가입 성공 시 리다이렉트 예상
                .andExpect(redirectedUrl("/public/signupComple")); // 회원가입 후 로그인 페이지로 리다이렉트

        // 회원이 실제로 DB에 저장되었는지 확인
        assertTrue(userRepository.existsByUsername("testuser"));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 유효성 검사")
    public void testSignUpValidationFailure() throws Exception {
        mockMvc.perform(post("/public/signup")
                        .param("username", "abc") // 5자 미만 (유효성 검사 실패)
                        .param("password", "pass") // 비밀번호 조건 불충족
                        .param("nickname", "a") // 닉네임 조건 불충족
                        .param("email", "invalid-email") // 이메일 형식 아님
                        .with(csrf()))
                .andExpect(status().isOk()) // 유효성 검사 실패 시 회원가입 페이지 유지
                .andExpect(model().attributeExists("valid_username")) // 필드별 유효성 메시지 확인
                .andExpect(model().attributeExists("valid_password"))
                .andExpect(model().attributeExists("valid_nickname"))
                .andExpect(model().attributeExists("valid_email"))
                .andExpect(view().name("public/user/signupForm")); // 다시 회원가입 페이지 반환
    }
}
