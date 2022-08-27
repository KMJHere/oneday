package com.oneday.web.repository;

import com.oneday.web.dto.ReplyDTO;
import com.oneday.web.service.ReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReplyServiceTests {
    @Autowired
    private ReplyService replyService;

    @Test
    public void testGetList() {
        Long bno = 71L;

        List<ReplyDTO> replyDTOList = replyService.getList(bno);

        replyDTOList.forEach(i -> System.out.println(i));
    }
}
