package music.memo.repository;

import music.memo.domain.Music;
import music.memo.domain.Review;
import music.memo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserAndMusic(User user, Music music);



}
