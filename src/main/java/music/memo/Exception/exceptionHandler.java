package music.memo.Exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class exceptionHandler {
    @ExceptionHandler(IllegalStateException.class)
    public String handleUsernameDuplicationException(IllegalStateException ex, Model model) {
        // 예외 메시지를 모델에 추가
        model.addAttribute("error_Duplication", ex.getMessage());
        // 회원가입 페이지로 포워딩
        return "public/user/signupForm";  // signup 페이지로 이동
    }
}
