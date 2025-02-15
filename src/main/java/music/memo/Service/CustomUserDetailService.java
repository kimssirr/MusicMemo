package music.memo.Service;

import lombok.RequiredArgsConstructor;
import music.memo.domain.User;
import music.memo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
// 스프링 시큐리티에서 사용자 정보 가져오는 인터페이스
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // 사용자 아이디로 사용자 정보 가져오는 메소드
    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(username));
    }


    public User loadUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));
    }

}