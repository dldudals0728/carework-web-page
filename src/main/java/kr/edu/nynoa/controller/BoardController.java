package kr.edu.nynoa.controller;

import kr.edu.nynoa.dto.BoardFormDto;
import kr.edu.nynoa.entity.Board;
import kr.edu.nynoa.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/addBoard")
    public ResponseEntity<Object> board(@Valid @RequestBody BoardFormDto boardFormDto, BindingResult bindingResult) {
        HashMap map = new HashMap<>();
        map.put("status", 200);
        System.out.println(boardFormDto.toString());

        Board board = Board.createBoard(boardFormDto);
        boardService.saveBoard(board);

        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @GetMapping("/selectBoard")
    public ResponseEntity<Object> selectBoard(@RequestParam(value = "category", required = true) String category,
                                              @RequestParam(value = "section", required = false) String section) {

        System.out.println(category);
        System.out.println(section);
        HashMap map = new HashMap<>();
        map.put("status", 200);
        List<Board> boardList = boardService.selectBoardList(category, section);
        map.put("boardList", boardList);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
