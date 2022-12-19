package kr.edu.nynoa.repository;

import kr.edu.nynoa.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByTitle(String title);

    Page<Board> findByCategory(String category, Pageable pageable);

    Page<Board> findByCategoryAndSection(String category, String section, Pageable pageable);

    Board findById(long id);
}
