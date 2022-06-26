package com.oneday.web.repository;

import com.oneday.web.entity.Guestbook;
import com.oneday.web.entity.Memo;
import com.oneday.web.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.catalina.users.GenericUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {
    @Autowired
    GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Test:" + i)
                    .content("Content 내용:" + i)
                    .writer("작성자:" + i)
                    .build();

            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    // moddate 갱신 확인
    @Test
    public void saveChangeDate() {
        Optional<Guestbook> result = guestbookRepository.findById(Integer.toUnsignedLong(601));

        if(result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.changeContent("내용변경");
            guestbook.changeTitle("제목변경");

            guestbookRepository.save(guestbook);
        }
    }
    
    // 단일 항목 검색, 제목에 1이라는 글자 포함 검색
    @Test
    public void selectQuery1() {
        // 내림차순으로 정렬(+ paging 처리)
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // 동적으로 처리하기 위해 Q도메인 클래스 가져옴, 필드를 변수로 사용 가능
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyWord = "1";

        // BooleanBuilder 생성, where 문에 들어가는 조건 컨테이너 역할 
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 조건 추가, 필드와 결합
        BooleanExpression expression = qGuestbook.title.contains(keyWord);

        // and 조건 결합
        booleanBuilder.and(expression);

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    // 복합 조건 검색, 제목 또는 내용에 1이 포함되고, 0보다 gno가 큰 리스트 검색
    @Test
    public void selectQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyWord = "1";

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyWord);
        BooleanExpression exContent = qGuestbook.content.contains(keyWord);
        BooleanExpression exAll = exContent.or(exTitle);

        booleanBuilder.and(exAll);
        booleanBuilder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

}
