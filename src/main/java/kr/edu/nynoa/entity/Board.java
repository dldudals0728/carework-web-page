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
}
