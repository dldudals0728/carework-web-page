package kr.edu.nynoa.entity;

import kr.edu.nynoa.constant.Role;
import kr.edu.nynoa.dto.AccountFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board {
    @Id
    @Column(name = "board_idx")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String texts;

    @Column(nullable = false)
    private String writer;

    @Lob
    @Column(nullable = false)
    private String publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role permission;


    public static User createBoard(AccountFormDto accountFormDto, PasswordEncoder passwordEncoder) {
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
        user.setRole(Role.ANONYMOUS);

        return user;
    }
}
