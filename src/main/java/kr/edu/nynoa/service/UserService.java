package kr.edu.nynoa.service;

import kr.edu.nynoa.dto.AccountFormDto;
import kr.edu.nynoa.dto.LoginFormDto;
import kr.edu.nynoa.entity.User;
import kr.edu.nynoa.manager.SessionManager;
import kr.edu.nynoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final SessionManager sessionManager;

    public User saveUser(User user) {

        return userRepository.save(user);
    }

    public User findUser(String id) {
        User user = userRepository.findByUserId(id);
        return user;
    }
    public User login(LoginFormDto loginFormDto, PasswordEncoder passwordEncoder, HttpServletResponse response) {
        System.out.println("user service loginUser function");
        User user = userRepository.findByUserId(loginFormDto.getId());

        if (user == null) {
            return null;
        }

        // id & pw로 db를 검색할 때 PasswordEncoder로 암호화 하면 매번 다른 해쉬 값으로 인해 다른 값이 나온다. matches로 확인!
        if (passwordEncoder.matches(loginFormDto.getPassword(), user.getPassword())) {
            sessionManager.createSession(user, response);
            return user;
        } else {
            return null;
        }
    }

    public User getUserSession(HttpServletRequest request) { return (User) sessionManager.getSession(request); }

    public void userLogout(HttpServletRequest request) { sessionManager.expire(request); }
}
