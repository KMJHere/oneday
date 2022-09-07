package com.oneday.web.repository;

import com.oneday.web.entity.Member2;
import com.oneday.web.entity.Movie;
import com.oneday.web.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // EntityGraph 어노테이션을 이용해 member 속성 로딩
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);
    
    // 회원을 이용해서 삭제하는 메서드
    @Modifying
    @Query("Delete From Review rw Where rw.member = :member")
    void deleteByMember(@Param("member") Member2 member);
}
