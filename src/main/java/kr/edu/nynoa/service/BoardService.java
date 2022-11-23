package kr.edu.nynoa.service;

import kr.edu.nynoa.entity.Board;
import kr.edu.nynoa.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public List<Board> selectBoardList(String category, String section) {
        if (section == null) {
            List<Board> boardList = boardRepository.findByCategory(category);
            return boardList;
        } else {
            List<Board> boardList = boardRepository.findByCategoryAndSection(category, section);
            return boardList;
        }
    }
}
