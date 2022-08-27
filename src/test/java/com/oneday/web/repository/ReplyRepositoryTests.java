package com.oneday.web.repository;

import com.oneday.web.entity.Board;
import com.oneday.web.entity.Member;
import com.oneday.web.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {
    @Autowired ReplyRepository replyRepository;

    @Test
    public void insertReply() {
        IntStream.rangeClosed(1, 300).forEach(i-> {
            // 1부터 100까지 랜덤 번호(최소값 1, 최대값 100)
            long bno = (long)(Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply  = Reply.builder()
                    .text("reply" + i)
                    .replyer("replyer" + i)
                    .board(board)
                    .build();

            replyRepository.save(reply);
        });
    }

    @Test
    public void testReadReply1() {
        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }

    @Test
    public void testListByBoard() {
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(97L).build());

        replyList.forEach(reply -> {System.out.println(reply);});
    }
}
