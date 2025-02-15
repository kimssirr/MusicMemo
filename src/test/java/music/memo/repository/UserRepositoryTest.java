package music.memo.repository;

import music.memo.domain.Music;
import music.memo.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MusicRepository musicRepository;

    @Test
    public void resister() throws Exception {
        //given
        User user = new User();
        user.setUsername("user1");
        user.setPassword("user1password");
        userRepository.save(user);
        //when
        List<User> userList = userRepository.findAll();

        //then
    }

    @Test
    public void testFindByUser() {
        User user = userRepository.findByUsername("user1")
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Music> musicList = musicRepository.findByUser(user);

        assertFalse(musicList.isEmpty()); // 음악 데이터가 있어야 함
        System.out.println("조회된 음악 개수: " + musicList.size());
    }

}