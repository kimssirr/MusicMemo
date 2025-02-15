package music.memo.repository;

import music.memo.domain.Music;
import music.memo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

    List<Music> findByUser(User user);

    boolean existsByMusicTitleAndArtist(String musicTitle, String artist);

    Optional<Music> findById(Long id);
}
