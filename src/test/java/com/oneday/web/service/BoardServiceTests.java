package com.oneday.web.service;

import com.oneday.web.dto.BoardDTO;
import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        BoardDTO dto = BoardDTO.builder()
                .title("Test")
                .content("Test..ing")
                .writerEmail("user12@kmj.com")
                .build();

        Long bno = boardService.register(dto);
    }

    // 게시물 조회 테스트
    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> pageResultDTO = boardService.getList(pageRequestDTO);

        for (BoardDTO boardDTO : pageResultDTO.getDtoList()) {
            System.out.println(boardDTO);
        }
    }

    // 게시물 단건 조회 테스트
    @Test
    public void testGet() {
        Long bno = 100L;

        BoardDTO boardDTO = boardService.get(bno);

        System.out.println(boardDTO);
    }

    // 삭제 테스트
    @Test
    public void testRemove() {
        Long bno = 97L;

        boardService.removeWithReplies(bno);
    }

    @Test
    public void testModify() {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목변경쓰")
                .content("내용 변경쓰~")
                .build();

        boardService.modify(boardDTO);
    }
}
