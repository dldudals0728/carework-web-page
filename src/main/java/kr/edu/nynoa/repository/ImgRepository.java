package kr.edu.nynoa.repository;

import kr.edu.nynoa.entity.Img;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgRepository extends JpaRepository<Img, Long> {
    List<Img> findByMappingBoard(String mappingBoard);
}
