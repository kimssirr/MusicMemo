package music.memo.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class publicController {
    @GetMapping("/")
    public String home() {
        log.info("home controller");
        return "public/home";
    }


}
