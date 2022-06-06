package com.oneday.web.repository;

import com.oneday.web.entity.Memo;
import org.hibernate.annotations.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import javax.transaction.Transactional;
import java.util.List;

// JpaRepository 인터페이스 상속, 엔티티 타입(Memo 크래스 타입) @Id의 타입 지정(Long) => Spring Data Jpa는 인터페이스 선언 시 자동으로 스프링의 빈으로 등록
public interface MemoRepository extends JpaRepository<Memo, Long> {
    // 쿼리 메서드 확인
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // 쿼리 메서드 확인2 , 메서드 선언 간결하게 하기 위해 Pageable 파라미터 추가..
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // deleteBy 삭제처리
    void deleteMemoByMnoLessThan(Long num);

    // @Query
    @Query("Select m From Memo m Order By m.mno Desc")
    List<Memo> getListDesc();

    // @Query + Pageable
    @Query(value = "Select m From Memo m Where m.mno > :mno",
            countQuery = "Select Count(m) From Memo m Where m.mno > :mno")
    Page<Memo> getListWithPageableDesc(Long mno, Pageable pageable);

    // @Query + Pageable => Object 반환 (필요한 엔티티 타입 데이터 추출)
    @Query(value = "Select m.mno, Current_Date From Memo m Where m.mno > :mno",
            countQuery = "Select Count(m) From Memo m Where m.mno > :mno")
    Page<Object[]> getListWithPageableObject(Long mno, Pageable pageable);

    // 파라미터 : 이용
    @Transactional
    @Modifying
    @Query("Update Memo m Set m.memoText = :memoText Where m.mno = :mno")
    int updateMemoText1(@Param("mno") Long mno, @Param("memoText") String memoText);

    // 파라미터 :# 이용 => 객체 전달
    @Query("Update Memo m Set m.memoText = :#{#param.memoText} Where m.mno = :#{#param.mno}")
    int updateMemoText2(@Param("param") Memo memo);

    // Nataive SQL 속성 확인
    @Query(value = "Select * From tbl_memo Where mno > 0", nativeQuery = true)
    List<Object[]> getNativeSql();
}
