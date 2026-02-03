package com.stock.webpage.api;

import com.stock.webpage.dto.*;
import com.stock.webpage.mapper.MyEtfItemHistoryMapper;
import com.stock.webpage.service.MyEtfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myetf")
public class MyEtfApiController {

    private final MyEtfService myEtfService;
    private final MyEtfItemHistoryMapper historyMapper;

    // =========================
    // 내가 만든 ETF 목록
    // =========================
    @GetMapping("/list")
    public PageResponseDTO<MyEtfSummaryDTO> myEtfList(
            @AuthenticationPrincipal User user,
            PageRequestDTO pageRequestDTO
    ) {
        return myEtfService.getMyEtfList(
                user.getUsername(),
                pageRequestDTO
        );
    }

    // =========================
    // ETF 생성
    // =========================
    @PostMapping("/create")
    public ResponseEntity<?> createEtf(
            @AuthenticationPrincipal User user,
            @RequestBody MyEtfCreateRequestDTO request
    ) {
        try {
            myEtfService.createEtf(user.getUsername(), request);
            return ResponseEntity.ok().build();

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // =========================
    // ETF 상세
    // =========================
    @GetMapping("/detail")
    public MyEtfDetailResponseDTO etfDetail(
            @AuthenticationPrincipal User user,
            @RequestParam String etfName
    ) {
        String userId = user.getUsername();

        return MyEtfDetailResponseDTO.builder()
                .etfName(etfName)
                .etfDescription(
                        myEtfService.getEtfDescription(userId, etfName)
                )
                .summary(
                        myEtfService.getEtfDetailSummary(userId, etfName)
                )
                .itemList(
                        myEtfService.getEtfItemList(userId, etfName)
                )
                .build();
    }

    // =========================
    // ETF 편집
    // =========================
    @PostMapping("/edit")
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
    public List<MyEtfItemHistoryDTO> getRestoreHistory(
            @AuthenticationPrincipal User user,
            @RequestParam String etfName
    ) {
        return myEtfService.getEtfItemRestoreHistory(
                user.getUsername(),
                etfName
        );
    }

    // =========================
    // ETF 종목 복구
    // =========================
    @PostMapping("/restore")
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
