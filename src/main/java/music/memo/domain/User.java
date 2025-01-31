package music.memo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20) // 5~20자
    private String username;
    @Column(length = 16) // 8~16자
    private String password;
    @Column(length = 10)
    private String nickname;
    private String email;
    private LocalDateTime createAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Music> musicList; // 사용자가 가진 음악 목록

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
