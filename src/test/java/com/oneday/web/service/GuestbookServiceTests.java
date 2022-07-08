package com.oneday.web.service;

import com.oneday.web.dto.GuestbookDTO;
import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.dto.PageResultDTO;
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
    
    // 엔티티 객체 > DTO 객체 변환되었는지 확인
    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<GuestbookDTO, Guestbook>  pageResultDTO = guestbookService.getList(pageRequestDTO);

        for(GuestbookDTO guestbookDTO : pageResultDTO.getDtoList()) {
            // GuestbookDTO 타입으로 출력
            System.out.println(guestbookDTO);
        }
    }

    // 페이징 처리 테스트 코드 작성
    @Test
    public void testList2() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("----------------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList())
        System.out.println(guestbookDTO);

        System.out.println("----------------------");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    public void testSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10)
                .type("tc") // t, c, tc,tcw..
                .keyword("한글")
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDto = guestbookService.getList(pageRequestDTO);

        System.out.println("PREV:" + resultDto.isPrev());
        System.out.println("NEXT:" + resultDto.isNext());
        System.out.println("TOTAL:" + resultDto.getTotalPage());

        System.out.println("-----------------------------------");
        for(GuestbookDTO guestbookDTO : resultDto.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("====================================");
        resultDto.getPageList().forEach(i -> System.out.println(i));
    }

}
