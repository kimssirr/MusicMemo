package music.memo.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import music.memo.Service.UserService;
import music.memo.dto.UserSignUpDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/public")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") UserSignUpDto dto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(result);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            model.addAttribute("user", dto); // 검사 실패시 입력 데이터 값을 유지
            log.info("user sign up fail");
            return "public/user/signupForm";
        }

        Map<String, String> duplicationErrors = userService.checkDuplication(dto);
        if (!duplicationErrors.isEmpty()) {
            for (String key : duplicationErrors.keySet()) {
                model.addAttribute(key, duplicationErrors.get(key));
            }
            model.addAttribute("user", dto);
            log.info("user sign up Duplication");
            return "public/user/signupForm";
        }

        userService.save(dto);  // 회원 가입 메서드 호출
        log.info("user sign up success");
        return "redirect:/public/signupComple";   // 회원 가입이 완료된 이후 완료 페이지
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new UserSignUpDto()); // 빈 DTO 객체 전달
        return "public/user/signupForm";
    }

    @GetMapping("/signupComple")
    public String signupComplete() {
        return "public/user/signupComple";  // 회원가입 완료 페이지
    }

    @GetMapping("/loginForm")
    public String login() {
        return "public/user/loginForm";
    }


}