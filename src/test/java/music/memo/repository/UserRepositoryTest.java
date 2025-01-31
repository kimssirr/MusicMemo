package music.memo.repository;

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
}