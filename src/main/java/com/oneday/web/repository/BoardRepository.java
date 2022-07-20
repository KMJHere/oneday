package com.oneday.web.repository;

import com.oneday.web.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 한개의 로우(Object) 내에 Object[]로 나옴
    @Query("Select b, w From Board b left join b.writer w Where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("Select b, r From Board b left join Reply r On r.board = b Where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);
}
