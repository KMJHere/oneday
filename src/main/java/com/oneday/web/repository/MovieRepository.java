package com.oneday.web.repository;

import com.oneday.web.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // 리뷰 평균 평점과 갯수 구하는 쿼리(Moive => MovieImage, Review 테이블 조인)
    @Query("Select m, mi, Avg(Coalesce(r.grade, 0)), Count(Distinct r) From Movie m " +
                "Left Outer Join MovieImage mi on mi.movie = m " +
                "Left Outer Join Review r On r.movie = m Group By m "
            )
    Page<Object[]> getListPage(Pageable pageable); // 페이지 처리

    @Query("Select m, mi, avg(coalesce(r.grade, 0)), count(r) " +
                "From Movie m Left Outer Join MovieImage mi on mi.movie = m " +
                "Left Outer Join Review r on r.movie = m " +
                "Where 1 =1 And m.mno = :mno Group By mi"
            )
    List<Object[]> getMovieWithAll(@Param("mno") Long mno); // 특정 리스트 조회
}
