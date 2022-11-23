package kr.edu.nynoa.repository;

import kr.edu.nynoa.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByTitle(String title);

    List<Board> findByCategory(String category);

    List<Board> findByCategoryAndSection(String category, String section);
}
