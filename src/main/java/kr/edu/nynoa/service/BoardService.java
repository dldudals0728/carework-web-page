package kr.edu.nynoa.service;

import kr.edu.nynoa.constant.Role;
import kr.edu.nynoa.dto.BoardFormDto;
import kr.edu.nynoa.entity.Board;
import kr.edu.nynoa.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board createBoard(BoardFormDto boardFormDto) {
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

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public Page<Board> selectBoardList(String category, String section, Pageable pageable) {
        this.boardTestCreate();
        if (section == null) {
            Page<Board> boardList = boardRepository.findByCategory(category, pageable);
            return boardList;
        } else {
            Page<Board> boardList = boardRepository.findByCategoryAndSection(category, section, pageable);
            return boardList;
        }
    }

    public Board getBoardContent(long id) {
        Board board = boardRepository.findById(id);

        return board;
    }

    public Board updateBoard(BoardFormDto boardFormDto, String boardIdx) {
        Board board = boardRepository.findById(Long.parseLong(boardIdx));
        if (board == null) {
            return null;
        }
        board.setCategory(boardFormDto.getCategory());
        board.setSection(boardFormDto.getSection());
        board.setTitle(boardFormDto.getTitle());
        board.setText(boardFormDto.getText());
        board.setWriter(boardFormDto.getWriter());
        board.setPublishedDate(LocalDateTime.now());
//        board.setPermission(Role.ANONYMOUS);
        Board savedBoard = boardRepository.save(board);
        return savedBoard;
    }

    public void deleteBoard(String boardIdx) {
//        Board deletedBoard = boardRepository.deleteById(Long.parseLong(boardIdx));
        boardRepository.deleteById(Long.parseLong(boardIdx));
    }


    public void boardTestCreate() {
        List<Board> boardList = boardRepository.findAll();

        if (boardList.size() > 20) {
            return;
        }
        int max = 100;

        for (int i=0; i < max; i++) {
            Board board = new Board();
            String section;
            board.setWriter("이영민");
            board.setViewCount(i);
            board.setPermission(Role.ANONYMOUS);
            board.setPublishedDate(LocalDateTime.now());
            board.setCategory("classinfo");
            if( i < 15) {
                section = "주간";
            } else {
                section = "야간";
            }
            board.setSection(section);
            board.setTitle(section + " - " + (i + 1));
            board.setText("<p>" + section + " - " + (i + 1) + " 본문입니다." + "</p>");

            boardRepository.save(board);
        }
    }
}
