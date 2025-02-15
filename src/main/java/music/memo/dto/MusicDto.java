package music.memo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import music.memo.domain.Music;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicDto {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(min = 1, max = 50)
    private String musicTitle;

    @NotBlank(message = "아티스트는 필수 입력 값입니다.")
    @Size(min = 1, max = 30)
    private String artist;

    @Size(max = 20)
    private String genre;

    @Min(value = 1800, message = "1800년 이상으로 입력해주세요.")
    @Max(value = 2100, message = "2100년 이하로 입력해주세요.")
    private Integer releaseYear;

    public  Music toEntity() {
        return Music.builder()
                .musicTitle(musicTitle)
                .artist(artist)
                .genre(genre)
                .releaseYear(releaseYear)
                .build();
    }


}
