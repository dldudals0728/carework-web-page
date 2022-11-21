package kr.edu.nynoa.entity;

import kr.edu.nynoa.constant.Role;
import kr.edu.nynoa.dto.AccountFormDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class User {

    @Id
    @Column(name = "user_idx")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 14)
    private String RRN;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(nullable = false, name = "user_id", unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Lob
    @Column(nullable = false)
    private String address;

    @Column(name = "class_number")
    private String classNumber;

    @Column(name = "class_time")
    private String classTime;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static User createUser(AccountFormDto accountFormDto, PasswordEncoder passwordEncoder) {
        System.out.println("UserService createUser");
        System.out.println("IN UserService toString");
        System.out.println(accountFormDto.toString());
        User user = new User();
        user.setName(accountFormDto.getName());
        user.setRRN(accountFormDto.getUserRRN());
        user.setPhone(accountFormDto.getPhone());
        user.setUserId(accountFormDto.getId());

        String password = passwordEncoder.encode(accountFormDto.getPassword());
        user.setPassword(password);

        user.setAddress(accountFormDto.getAddress());
        user.setClassNumber(accountFormDto.getClassNumber());
        user.setClassTime(accountFormDto.getClassTime());
        user.setRole(Role.USER);

        return user;
    }
}
