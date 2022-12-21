package kr.edu.nynoa.controller;

import kr.edu.nynoa.entity.Img;
import kr.edu.nynoa.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/img")
@RequiredArgsConstructor
public class ImgController {

    private final ImgService imgService;
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadImage(HttpServletRequest request,
                                              @RequestParam(value = "file") MultipartFile[] files,
                                              @RequestParam(value = "mappingBoard") String mappingBoard,
                                              @RequestParam(value = "comment") String comment) {
        HashMap map = new HashMap<>();
        Img img = imgService.uploadImg(files, mappingBoard, comment);
        if (img == null) {
            map.put("status", 500);
        } else {
            map.put("status", 200);
            map.put("savedImg", img);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/getImg")
    public ResponseEntity<Object> getImage(@RequestParam(value = "boardIdx") long boardIdx) {
        HashMap map = new HashMap<>();

        List<Img> img = imgService.getBoardImage(Long.toString(boardIdx));

        if (img == null) {
            map.put("status", 500);
        } else {
            map.put("status", 200);
        }
        map.put("images", img);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
