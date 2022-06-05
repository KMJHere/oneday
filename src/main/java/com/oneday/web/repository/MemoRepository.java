package com.oneday.web.repository;

import com.oneday.web.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

// JpaRepository 인터페이스 상속, 엔티티 타입(Memo 크래스 타입) @Id의 타입 지정(Long) => Spring Data Jpa는 인터페이스 선언 시 자동으로 스프링의 빈으로 등록
public interface MemoRepository extends JpaRepository<Memo, Long> {
    // 쿼리 메서드 확인
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // 쿼리 메서드 확인2 , 메서드 선언 간결하게 하기 위해 Pageable 파라미터 추가..
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
}
