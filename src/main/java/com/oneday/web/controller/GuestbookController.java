package com.oneday.web.controller;

import com.oneday.web.dto.GuestbookDTO;
import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor // 자동 주입을 위한 annotaion
public class GuestbookController {
    private final GuestbookService guestbookService; // final로 선언

    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/list";
    }

    // 조회
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("본격적인...후후" + pageRequestDTO);

        model.addAttribute("result", guestbookService.getList(pageRequestDTO));
    }

    // 상세 조회, @ModelAttribute 어노테이션: 객체 자동 생성
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("상세조회..");

        GuestbookDTO dto = guestbookService.read(gno);

        model.addAttribute("dto", dto);
    }

    // 등록 상세 조회
    @GetMapping("/register")
    public void register() {
        log.info("등록 상세..");
    }

    // 등록
    @PostMapping("/register")
    public String registerPost(GuestbookDTO guestbookDTO, RedirectAttributes redirectAttributes) {
        log.info("등록...");

        // 새로 추가된 엔티티의 번호 gno 저장
        Long gno = guestbookService.register(guestbookDTO);


        // 단 한번만 데이터를 전달하는 용도로 addFlashAttribute 사용
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }
}
