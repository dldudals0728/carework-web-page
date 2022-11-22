package kr.edu.nynoa.repository;

import kr.edu.nynoa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String id);

    User findByUserIdAndPassword(String id, String password);
}
