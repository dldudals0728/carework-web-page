package kr.edu.nynoa.entity;

import kr.edu.nynoa.constant.Role;
import kr.edu.nynoa.dto.AccountFormDto;
import kr.edu.nynoa.dto.BoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String text;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role permission;

    public static Board createBoard(BoardFormDto boardFormDto) {
        Board board = new Board();
        board.setCategory(boardFormDto.getCategory());
        board.setSection(boardFormDto.getSection());
        board.setTitle(boardFormDto.getTitle());
        board.setText(boardFormDto.getText());
        board.setWriter(boardFormDto.getWriter());
        board.setPublishedDate(LocalDateTime.now());
        board.setPermission(Role.ANONYMOUS);
        return board;
    }
}
