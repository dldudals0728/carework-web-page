package kr.edu.nynoa.repository;

import kr.edu.nynoa.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
