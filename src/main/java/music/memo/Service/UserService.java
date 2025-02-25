package music.memo.Service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import music.memo.domain.User;
import music.memo.dto.UserSignUpDto;
import music.memo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /* 회원가입 시, 유효성 체크 */
    public Map<String, String> validateHandling(BindingResult result) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : result.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    /**
     회원가입
     */


    @Transactional(readOnly = false)
    public Long save(UserSignUpDto dto) {
        // 패스워드 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(dto.getPassword());

        // User 엔티티 생성
        User user = dto.toEntity();

        // 사용자 저장
        return userRepository.save(user).getId(); // ID 반환
    }

    public Map<String, String> checkDuplication(UserSignUpDto dto) {
        Map<String, String> errors = new HashMap<>();

        if (checkEmailDuplication(dto)) {
            errors.put("Duplication_email", "이미 존재하는 이메일입니다.");
        }
        if (checkNicknameDuplication(dto)) {
            errors.put("Duplication_nick", "이미 존재하는 닉네임입니다.");
        }
        if (checkUsernameDuplication(dto)) {
            errors.put("Duplication_username", "이미 존재하는 아이디입니다.");
        }

        return errors; // 중복된 필드와 메시지를 반환
    }

    // 아이디 중복 검사
    @Transactional(readOnly = true)
    public boolean checkUsernameDuplication(UserSignUpDto dto) {
        return userRepository.existsByUsername(dto.getUsername());
    }

    // 닉네임 중복 검사
    @Transactional(readOnly = true)
    public boolean checkNicknameDuplication(UserSignUpDto dto) {
        return userRepository.existsByNickname(dto.getNickname());
    }

    // 이메일 중복 검사
    @Transactional(readOnly = true)
    public boolean checkEmailDuplication(UserSignUpDto dto) {
        return userRepository.existsByEmail(dto.getEmail());
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return user;
    }

}
