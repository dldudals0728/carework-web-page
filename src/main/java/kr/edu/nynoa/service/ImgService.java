package kr.edu.nynoa.service;

import kr.edu.nynoa.entity.Img;
import kr.edu.nynoa.repository.ImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImgService {
    private final ImgRepository imgRepository;
    String filePath = "/Users/dev/Desktop/workspace/CTSoft/carework-web-page/src/main/resources/static/images/";

    public Img uploadImg(MultipartFile[] files, String mappingBoard,String comment) {
        String fileNames = "";

        String originFileName = files[0].getOriginalFilename();
        long fileSize = files[0].getSize();
        String safeFile = System.currentTimeMillis() + originFileName;

        File f1 = new File(filePath + safeFile);
        try {
            // transferTo: IOException 처리 필수
            files[0].transferTo(f1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("img save error");
        }

        // .builder 함수를 사용하려면 Entity class에 @Builder annotation 필요.
//        final Img img = Img.builder()
//                .imgName(safeFile)
//                .mappingBoard(mappingBoard)
//                .build();
        final Img img = new Img();
        img.setImgName(safeFile);
        img.setMappingBoard(mappingBoard);

        return imgRepository.save(img);
    }

    public List<Img> getBoardImage(String boardIdx) {
        return imgRepository.findByMappingBoard(boardIdx);
    }
}
