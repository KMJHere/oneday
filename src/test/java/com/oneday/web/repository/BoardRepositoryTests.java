package com.oneday.web.repository;

import com.oneday.web.entity.Board;
import com.oneday.web.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {
    @Autowired BoardRepository boardRepository;

    @Test
    public void insertBoard() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@kmj.com").build();

            Board board = Board.builder()
                            .title("제목" + i)
                            .content("내용" + i)
                            .writer(member)
                            .build();

            boardRepository.save(board);
        });
    }

    @Transactional
    @Test
    public void testReadBoard1() {
        Optional<Board> result = boardRepository.findById(Integer.toUnsignedLong(1));

        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());
    }

    @Test
    public void testWithWriter() {
        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[])result;

        System.out.println(arr.toString());
    }

    @Test
    public void testWithBoardReply() {
        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for(Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    // 댓글 갯수 확인
    @Test
    public void testWithReplyCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(idx -> {
            Object[] aoDat = (Object[])idx;

            System.out.println(Arrays.toString(aoDat));
        });
    }

    @Test
    public void testRead3() {
        Object result = boardRepository.getBoardByBno(100L);

        Object[] aoDat = (Object[])result;

        System.out.print(Arrays.toString(aoDat));
    }

    // Querydsl 테스트(JPQLQuery)
    @Test
    public void testSearch1() {
        boardRepository.search1();
    }
}
