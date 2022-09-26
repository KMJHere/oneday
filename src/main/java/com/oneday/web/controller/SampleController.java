package com.oneday.web.controller;

import com.oneday.web.dto.SampleDTO;
import com.oneday.web.security.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {
    @GetMapping("/all")
    @PreAuthorize("permitAll()")
    public void exAll() {
        log.info("전체가능..");
    }

    // @AuthenticationPrincipal 어노테이션을 이용해 회원 정보 출력
    @GetMapping("/member")
    @PreAuthorize("hasRole('USER')")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("멤버가능..");

        log.info("member Info: " + clubAuthMemberDTO);

    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void exAdmin() {
        log.info("관리자가능..");
    }

    @GetMapping("/ex1")
    public void ex1() {
        log.info("thymeleaf.. 확인중");
    }

    @GetMapping({"/ex2", "/exLink"})
    public void exModel(Model model) {
        List<SampleDTO> list = IntStream.rangeClosed(1, 20).asLongStream().mapToObj(i -> {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First..." + i)
                    .last("Last..." + i)
                    .regTime(LocalDateTime.now())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }

    @GetMapping({"/exInline"})
    public String exInline(RedirectAttributes redirectAttributes) {
        log.info("exInline...");

        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("First..1")
                .last("Lst..")
                .regTime(LocalDateTime.now())
                .build();

        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);

        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3() {
        log.info("ex3");
    }

    @GetMapping({"/exLayout1", "/exLayout2", "/exTemplate", "/exSidebar"})
    public void exLayout1() {
        log.info("exLayout...");
    }

    // @PreAuthorize 어노테이션 확인
    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq \"user95@kmj.org\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember) {
        log.info("exMemberOnly");
        log.info("clubAuthMemberDTO: " + clubAuthMember);

        return "/sample/admin";
    }
}
