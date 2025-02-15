package music.memo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "music_id"}) }) //특정 음악에 대한 리뷰 1개
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long id;
    @Column(nullable = false, length = 20)
    private String reviewTitle;
    private LocalDateTime reviewDate;
    @Column(nullable = false)
    private String content;
    @Column(length = 1)
    private Integer rating;

    // 외래 키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id", nullable = false)
    private Music music;

    @Builder
    public Review(String reviewTitle, String content, int rating) {
        this.reviewTitle = reviewTitle;
        this.content = content;
        this.rating = rating;
    }
}
