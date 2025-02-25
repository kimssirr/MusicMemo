package music.memo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import music.memo.dto.MusicDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="music_id")
    private Long id;
    @Column(nullable = false)
    private String musicTitle;
    @Column(nullable = false)
    private String artist;
    private String genre;
    private Integer releaseYear;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Music(String musicTitle, String artist, String genre, int releaseYear) {
        this.musicTitle = musicTitle;
        this.artist = artist;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }


}
