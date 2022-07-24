package com.oneday.web.service;

import com.oneday.web.dto.BoardDTO;
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
}
