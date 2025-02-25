package music.memo.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import music.memo.Service.CustomUserDetailService;
import music.memo.Service.MusicService;
import music.memo.domain.Music;
import music.memo.domain.User;
import music.memo.dto.MusicDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/music")
public class MusicController {
    private final MusicService musicService;
    private final CustomUserDetailService userService;
    @GetMapping("/signup")
    public String musicSignUpForm(Model model) {
        model.addAttribute("music",new MusicDto());
        return "private/music/musicForm";
    }

    @PostMapping("/signup")
    public String musicSignUp(@Valid @ModelAttribute("music") MusicDto dto, BindingResult result, @AuthenticationPrincipal UserDetails userDetails, Model model){
        if(result.hasErrors()) {
            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = musicService.validateHandling(result);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            model.addAttribute("music",dto); // 오류시 입력 데이터 유지
            log.info("music sign up fail");
            return "private/music/musicForm";
        }

        Map<String, String> duplicationErrors = musicService.checkDuplication(dto);
        if (!duplicationErrors.isEmpty()) {
            for (String key : duplicationErrors.keySet()) {
                model.addAttribute(key, duplicationErrors.get(key));
            }
            model.addAttribute("music", dto);
            log.info("music sign up Duplication");
            return "private/music/musicForm";
        }

        musicService.save(dto,userDetails.getUsername());  // 회원 가입 메서드 호출
        log.info("music sign up success");
        return "redirect:/music/musicComple";
    }

    @GetMapping("/musicComple")
    public String signupComplete() {
        return "private/music/musicComple";  // 회원가입 완료 페이지
    }


    @GetMapping("/list")
    public String musicList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Music> musicList = musicService.findMusicByUser(userDetails.getUsername());
        if(musicList.isEmpty()) {
            model.addAttribute("musicListNull","등록된 음악이 없습니다.");
            return "private/music/musicLists";
        }
        model.addAttribute("musicList", musicList);
        User user = userService.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        return "private/music/musicLists";
    }

    @PostMapping("/delete/{musicId}")
    public String deleteReview(@PathVariable Long musicId) {
        musicService.deleteReview(musicId);
        return "redirect:/music/list"; // 삭제 후 목록 페이지로 이동
    }
}
