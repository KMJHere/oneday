package com.oneday.web.service;

import com.oneday.web.dto.GuestbookDTO;
import com.oneday.web.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService guestbookService;

    @Test
    public void testRegister() {
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("sample regis")
                .content("Content")
                .writer("mj")
                .build();

        System.out.println(guestbookService.register(guestbookDTO));
    }
}
