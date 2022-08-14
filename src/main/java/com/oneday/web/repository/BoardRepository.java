package com.oneday.web.repository;

import com.oneday.web.entity.Board;
import com.oneday.web.repository.Search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
    // 한개의 로우(Object) 내에 Object[]로 나옴
    @Query("Select b, w From Board b left join b.writer w Where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("Select b, r From Board b left join Reply r On r.board = b Where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);


    // 페이징처리 + 목록 + 댓글수 => 목록
    @Query(value= "Select b, w, Count(r) "
                    + "From Board b "
                    + "Left Join b.writer w "
                    + "Left Join Reply r On r.board = b "
                    + "Group By b",
            countQuery = "Select Count(b) From Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록 화면에 필요한 데이터
    
    // 특정 게시물, 목록 상세
    @Query(value= "Select b, w, Count(r) "
                    + "From Board b "
                    + "Left Join b.writer w "
                    + "Left Join Reply r On r.board = b "
                    + "Where 1 = 1 "
                    + "And b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
