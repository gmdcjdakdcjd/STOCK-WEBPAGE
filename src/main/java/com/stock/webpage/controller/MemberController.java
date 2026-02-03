package com.stock.webpage.controller;

import com.stock.webpage.dto.MemberJoinDTO;
import com.stock.webpage.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /* =========================
       로그인 페이지
       ========================= */
    @GetMapping("/login")
    public void loginGET(String error, String logout) {

        log.info("login get...");
        log.info("error: {}", error);
        log.info("logout: {}", logout);

        if (logout != null) {
            log.info("user logout");
        }
    }

    /* =========================
       회원가입 페이지
       ========================= */
    @GetMapping("/join")
    public void joinGET() {
        log.info("join get...");
    }

    /* =========================
       회원가입 처리
       ========================= */
    @PostMapping("/join")
    public String joinPOST(
            MemberJoinDTO memberJoinDTO,
            RedirectAttributes redirectAttributes
    ) {

        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);

        } catch (MemberService.MidExistException e) {

            log.warn("MID already exists: {}", memberJoinDTO.getMid());
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";

        } catch (MemberService.EmailExistException e) {

            log.warn("EMAIL already exists: {}", memberJoinDTO.getEmail());
            redirectAttributes.addFlashAttribute("error", "email");
            return "redirect:/member/join";

        } catch (Exception e) {

            log.error("join error", e);
            redirectAttributes.addFlashAttribute("error", "unknown");
            return "redirect:/member/join";
        }

        // 성공 → login + joined 파라미터
        return "redirect:/member/login?joined=true";
    }


    @GetMapping("/checkMid")
    @ResponseBody
    public boolean checkMid(@RequestParam String mid) {
        return memberService.isMidAvailable(mid);
    }

    @GetMapping("/checkEmail")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        return memberService.isEmailAvailable(email);
    }

}
