package com.oneday.web.controller;

import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("본격적인...후후" + pageRequestDTO);

        model.addAttribute("result", guestbookService.getList(pageRequestDTO));
    }
}
