package com.stock.webpage.controller;

import com.stock.webpage.dto.*;
import com.stock.webpage.mapper.MyEtfItemHistoryMapper;
import com.stock.webpage.service.MyEtfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myetf")
public class MyEtfController {

    private final MyEtfService myEtfService;
    private final MyEtfItemHistoryMapper historyMapper;

    // =========================
    // 내가 만든 ETF 목록
    // =========================
    @GetMapping("/list")
    public String myEtfList(
            @AuthenticationPrincipal User user,
            PageRequestDTO pageRequestDTO,
            Model model
    ) {
        model.addAttribute(
                "result",
                myEtfService.getMyEtfList(user.getUsername(), pageRequestDTO)
        );
        return "myetf/list";
    }


    // =========================
    // ETF 생성
    // =========================
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createEtf(
            @AuthenticationPrincipal User user,
            @RequestBody MyEtfCreateRequestDTO request
    ) {
        try {
            myEtfService.createEtf(user.getUsername(), request);
            return ResponseEntity.ok().build();

        } catch (IllegalStateException e) {
            // ⭐ 여기
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409
                    .body(e.getMessage());
        }
    }



    // =========================
    // ETF 상세
    // =========================
    @GetMapping("/detail")
    public String etfDetail(
            @AuthenticationPrincipal User user,
            @RequestParam String etfName,
            Model model
    ) {
        String userId = user.getUsername();

        // ⭐ ViewDTO로 받는다
        List<MyEtfItemViewDTO> itemList =
                myEtfService.getEtfItemList(userId, etfName);

        model.addAttribute("etfName", etfName);
        model.addAttribute(
                "etfDescription",
                myEtfService.getEtfDescription(userId, etfName)
        );
        model.addAttribute(
                "summary",
                myEtfService.getEtfDetailSummary(userId, etfName)
        );
        model.addAttribute("itemList", itemList);

        return "myetf/my-etf-detail";
    }

    // =========================
    // ETF 편집
    // =========================
    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> editEtf(
            @AuthenticationPrincipal User user,
            @RequestBody MyEtfEditRequestDTO request
    ) {
        myEtfService.editEtf(user.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    // =========================
    // 삭제 히스토리 조회
    // =========================
    @GetMapping("/history")
    @ResponseBody
    public List<MyEtfItemHistoryDTO> getRestoreHistory(
            @AuthenticationPrincipal User user,
            @RequestParam String etfName
    ) {
        return historyMapper.selectByUserIdEtfNameRestoredYn(
                user.getUsername(),
                etfName,
                "N"
        );
    }

    // =========================
    // ETF 종목 복구
    // =========================
    @PostMapping("/restore")
    @ResponseBody
    public ResponseEntity<?> restoreEtf(
            @AuthenticationPrincipal User user,
            @RequestBody MyEtfRestoreRequestDTO request
    ) {
        myEtfService.restoreEtfItems(user.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    // =========================
    // ETF 삭제
    // =========================
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteEtf(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> body
    ) {
        myEtfService.deleteEtf(
                user.getUsername(),
                body.get("etfName")
        );
        return ResponseEntity.ok().build();
    }
}
