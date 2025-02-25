package music.memo.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import music.memo.dto.MusicDto;
import music.memo.repository.MusicRepository;
import music.memo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

@ExtendWith(MockitoExtension.class)
class MusicServiceTest {

    private Validator validator;

    @InjectMocks
    private MusicService musicService;

    @Mock
    private MusicRepository musicRepository;


    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 음악_유효성_검사() {
        // Given: 제목이 비어있음
        MusicDto musicDto = new MusicDto("", "Artist", "Pop", 2000);

        // When
        Set<ConstraintViolation<MusicDto>> violations = validator.validate(musicDto);
        BindingResult bindingResult = new BeanPropertyBindingResult(musicDto, "musicDto");

        for (ConstraintViolation<MusicDto> violation : violations) {
            bindingResult.rejectValue(violation.getPropertyPath().toString(), "valid_" + violation.getPropertyPath(), violation.getMessage());
        }

        Map<String, String> errors = musicService.validateHandling(bindingResult);

        // Then
        assertTrue(errors.containsKey("valid_musicTitle"), "valid_musicTitle 키가 존재하지 않습니다.");
        assertEquals("제목은 필수 입력 값입니다.", errors.get("valid_musicTitle"));
    }

    @Test
    public void 음악_중복_검사() {
        // Given
        MusicDto musicDto = new MusicDto("Song Title", "Artist Name", "Rock", 2020);
        when(musicRepository.existsByMusicTitleAndArtist(musicDto.getMusicTitle(), musicDto.getArtist())).thenReturn(true);

        // When
        Map<String, String> errors = musicService.checkDuplication(musicDto);

        // Then
        assertFalse(errors.isEmpty());
        assertEquals("이미 존재하는 음악입니다.", errors.get("Duplication_music"));

        verify(musicRepository, times(1)).existsByMusicTitleAndArtist(musicDto.getMusicTitle(), musicDto.getArtist());
    }


}
