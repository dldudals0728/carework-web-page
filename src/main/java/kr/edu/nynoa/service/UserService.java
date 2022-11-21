package kr.edu.nynoa.service;

import kr.edu.nynoa.entity.User;
import kr.edu.nynoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {

        return userRepository.save(user);
    }

//    public User findUser(User user) {
//        User checkUser = userRepository.findById(user.getUserId());
//        if (checkUser == null) {
//
//        }
//    }
}
