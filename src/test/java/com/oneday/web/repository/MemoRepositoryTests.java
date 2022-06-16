package com.oneday.web.repository;
import com.oneday.web.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;


import javax.persistence.Column;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    // findById()
    @Test
    public void testSelect() {
        // DB 존재하는 mnd
        Long mno = Long.valueOf(1);

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("--------------Div--------------");

        // Optional 객체 값이 존재하는지 판단
        if(result.isEmpty()) {
            //Optional 객체가 가지고 있는 value 값을 꺼내 옴
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    // getOne()
    @Test
    @Transactional
    public void testSelect2() {
        Long mno = Long.valueOf(1);
        Long mno2 = Long.valueOf(2);

        Memo memo = memoRepository.getOne(mno);
        Memo memo2 = memoRepository.getReferenceById(mno2);

        System.out.println("--------------Div--------------");
        System.out.println(memo);
        System.out.println(memo2);
    }

    // 페이징 처리
    @Test
    public void testPageDefault() {
        // 1페이지 10개, 페이지 처리 0부터 시작
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> page = memoRepository.findAll(pageable);

        System.out.println(page);

        System.out.println("--------------------Div--------------------");

        System.out.println("Total Pages: " + page.getTotalPages()); // 총 몇페이지인지

        System.out.println("Total Count: " + page.getTotalElements()); // 전체개수

        System.out.println("Page Number: " + page.getNumber()); // 현재 페이지 번호(0부터..)

        System.out.println("Page Size: " + page.getSize()); // 페이지당 데이터 건수

        System.out.println("next page YN: " + page.hasNext()); // 다음 페이지 존재여부

        System.out.println("first page YN: " + page.isFirst()); // 시작 페이지 존재여부

        System.out.println("--------------------Div--------------------");

        // 기본적인 정렬 확인
        for(Memo memo : page.getContent()) {
            System.out.println(memo);
        }
    }

    // 페이징 처리 + 정렬
    @Test
    public void testPageSort() {
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sort3 = sort1.and(sort2);

        // Pageable pageable = PageRequest.of(0, 10, sort1);
        Pageable pageable = PageRequest.of(0, 20, sort3);

        Page<Memo> page = memoRepository.findAll(pageable);

        // get() 메서드 사용할 경우 스트림 => 람다식 사용할 수 있어 가독성 좋음
        page.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    // 쿼리 메서드
    @Test
    public void testQueryMethod() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(Long.valueOf(70), Long.valueOf(80));

        for(Memo memo : list) {
            System.out.print(memo);
        }
    }

    @Test
    public void testQueryMethodPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> page = memoRepository.findByMnoBetween(Long.valueOf(70), Long.valueOf(80), pageable);

        page.get().forEach(memo -> System.out.println(memo));
    }

    @Test
    public void testQueryGetDesc() {
        memoRepository.getListDesc();
    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample.." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(Long.valueOf(1)).memoText("수정테스트..").build();

        System.out.println(memoRepository.save(memo));
    }

    // 데이터가 존재하지 않을 경우 EmptyResultDataAccessException 발생
    @Test
    public void testDelete() {
        Long mno = Long.valueOf(1);

        memoRepository.deleteById(mno);
    }

    @Test
    @Transactional
    @Commit
    public void testDeleteQueryMethod() {
        Long mno = Long.valueOf(2);

       memoRepository.deleteMemoByMnoLessThan(mno);
    }

    @Test
    public void testNativeSql() {
        List<Object[]> list = memoRepository.getNativeSql();

        for(Object[] arr : list) {
            for(Object obj : arr) {
                System.out.println("test" + obj);
            }
        }

    }
}
