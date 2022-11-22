package kr.edu.nynoa.service;

import kr.edu.nynoa.dto.AccountFormDto;
import kr.edu.nynoa.dto.LoginFormDto;
import kr.edu.nynoa.entity.User;
import kr.edu.nynoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {

        return userRepository.save(user);
    }

    public User findUser(String id) {
        User user = userRepository.findByUserId(id);
        return user;
    }
    public User loginUser(LoginFormDto loginFormDto, PasswordEncoder passwordEncoder) {
        User user = userRepository.findByUserId(loginFormDto.getId());

        // id & pw로 db를 검색할 때 PasswordEncoder로 암호화 하면 매번 다른 해쉬 값으로 인해 다른 값이 나온다. matches로 확인!
        if (passwordEncoder.matches(loginFormDto.getPassword(), user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
}
