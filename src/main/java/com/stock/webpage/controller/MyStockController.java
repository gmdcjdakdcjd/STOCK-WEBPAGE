package com.stock.webpage.controller;

import com.stock.webpage.dto.MyStockDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.service.MyStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mystock")
@RequiredArgsConstructor
public class MyStockController {

    private final MyStockService myStockService;

    /**
     * 관심종목 등록 (여러 개)
     */
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addMyStock(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestBody List<MyStockDTO> list) {

        String userId = user.getUsername();
        myStockService.addBatch(userId, list);

        return Map.of("result", "SUCCESS");
    }

    /**
     * 관심종목 목록 조회
     */
    @GetMapping("/list")
    public String myStockList(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            @RequestParam(defaultValue = "1") int krPage,
            @RequestParam(defaultValue = "1") int usPage,
            Model model) {

        String userId = user.getUsername();

        PageRequestDTO krPageRequest = PageRequestDTO.builder()
                .page(krPage)
                .size(10)
                .build();

        PageRequestDTO usPageRequest = PageRequestDTO.builder()
                .page(usPage)
                .size(10)
                .build();

        PageResponseDTO<MyStockDTO> krResult =
                myStockService.getMyStockListKR(userId, krPageRequest);

        PageResponseDTO<MyStockDTO> usResult =
                myStockService.getMyStockListUS(userId, usPageRequest);

        model.addAttribute("krResult", krResult);
        model.addAttribute("usResult", usResult);

        return "mystock/list";
    }


    /**
     * 관심종목 단일 삭제 (soft delete)
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> deleteMyStock(
            @PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

        myStockService.delete(id, user.getUsername());
        return Map.of("result", "SUCCESS");
    }

    /**
     * 삭제된 관심종목 목록
     */
    @GetMapping("/deleted")
    @ResponseBody
    public PageResponseDTO<MyStockDTO> deletedList(
            @AuthenticationPrincipal User user,
            PageRequestDTO pageRequestDTO
    ) {
        return myStockService.getDeletedListPaging(
                user.getUsername(), pageRequestDTO
        );
    }

    /**
     * 관심종목 복구
     */
    @PostMapping("/restore/{id}")
    @ResponseBody
    public Map<String, Object> restore(
            @PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {

        myStockService.restore(id, user.getUsername());
        return Map.of("result", "SUCCESS");
    }

    @GetMapping("/list/kr")
    @ResponseBody
    public PageResponseDTO<MyStockDTO> krList(
            @AuthenticationPrincipal User user,
            PageRequestDTO pageRequestDTO) {

        return myStockService.getMyStockListKR(user.getUsername(), pageRequestDTO);
    }

    @GetMapping("/list/us")
    @ResponseBody
    public PageResponseDTO<MyStockDTO> usList(
            @AuthenticationPrincipal User user,
            PageRequestDTO pageRequestDTO) {

        return myStockService.getMyStockListUS(user.getUsername(), pageRequestDTO);
    }

}
