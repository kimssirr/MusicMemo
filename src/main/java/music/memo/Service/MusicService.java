package music.memo.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import music.memo.domain.Music;
import music.memo.domain.User;
import music.memo.dto.MusicDto;
import music.memo.repository.MusicRepository;
import music.memo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;
    private final UserRepository userRepository;

    /**
     * 음악 저장
     */
    @Transactional(readOnly = false)
    public Long save(MusicDto dto, String username) {
        // Music 엔티티 생성
        Music music = dto.toEntity();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        music.setUser(user);
        // 음악 저장
        return musicRepository.save(music).getId(); // ID 반환
    }

    /* 음악 등록 시, 유효성 체크 */
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
     * 중복 검사 (음악 제목 + 아티스트)
     */
    public Map<String, String> checkDuplication(MusicDto dto) {
        Map<String, String> errors = new HashMap<>();

        if (checkMusicDuplication(dto)) {
            errors.put("Duplication_music", "이미 존재하는 음악입니다.");
        }

        return errors; // 중복된 필드와 메시지를 반환
    }

    // 음악 제목과 아티스트가 같은 음악이 존재하는지 확인
    @Transactional(readOnly = true)
    public boolean checkMusicDuplication(MusicDto dto) {
        return musicRepository.existsByMusicTitleAndArtist(dto.getMusicTitle(), dto.getArtist());
    }

    /**
     유저의 음악목록 찾기
     */
    public List<Music> findMusicByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return user.getMusicList();
    }

    /**
     * 유저의 음악목록 찾기
     */
    public Music findMusicByMusicID(Long id) {
        return musicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("음악을 찾을 수 없습니다. ID: " + id));
    }

    /**
     * 음악 삭제
     */
    public void deleteReview(Long musicId) {

        musicRepository.deleteById(musicId);
    }
}

