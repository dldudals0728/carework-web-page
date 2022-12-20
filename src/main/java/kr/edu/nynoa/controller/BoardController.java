package kr.edu.nynoa.controller;

import kr.edu.nynoa.dto.BoardFormDto;
import kr.edu.nynoa.entity.Board;
import kr.edu.nynoa.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

        Board board = boardService.createBoard(boardFormDto);
        boardService.saveBoard(board);

        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PostMapping("/updateBoard")
    public ResponseEntity<Object> updateBoard(@Valid @RequestBody BoardFormDto boardFormDto, @RequestParam(value = "boardIdx") String boardIdx) {
        HashMap map = new HashMap<>();
        System.out.println(boardFormDto.toString());
        System.out.println(boardIdx);
        Board board = boardService.updateBoard(boardFormDto, boardIdx);
        if (board == null) {
            map.put("status", 500);
            map.put("errorMessage", "there is no board content. can not update board.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else {
            map.put("status", 200);
            map.put("updateBoard", board);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/selectBoard")
    public ResponseEntity<Object> selectBoard(@RequestParam(value = "category", required = true) String category,
                                              @RequestParam(value = "section", required = false) String section,
                                              @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        HashMap map = new HashMap<>();
        map.put("status", 200);
        Page<Board> boardList = boardService.selectBoardList(category, section, pageable);
        System.out.println("======== page test ========");
        System.out.println("boardList.getSize(): " + boardList.getSize());
        System.out.println(boardList.getTotalElements());
//        boardService.boardTestCreate();
        map.put("boardList", boardList);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/getBoardContent")
    public ResponseEntity<Object> getBoardContent(@RequestParam(value = "boardIdx") String boardIdx) {
        HashMap map = new HashMap<>();
        long id = Long.parseLong(boardIdx);
        Board board = boardService.getBoardContent(id);
        if (board == null) {
            map.put("status", 500);
            map.put("errorMessage", "there is no content");
        } else {
            map.put("status", 200);
            map.put("board", board);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/increaseViewCount")
    public int increaseViewCount(@RequestParam(value = "boardIdx") String boardIdx) {
        return 0;
    }
}
