package com.stock.webpage.api;

import com.stock.webpage.dto.MyStockDTO;
import com.stock.webpage.dto.PageRequestDTO;
import com.stock.webpage.dto.PageResponseDTO;
import com.stock.webpage.service.MyStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mystock")
@RequiredArgsConstructor
public class MyStockApiController {

    private final MyStockService myStockService;

    /**
     * 관심종목 등록 (여러 개)
     */
    @PostMapping("/add")
    public Map<String, Object> addMyStock(
            @AuthenticationPrincipal User user,
            @RequestBody List<MyStockDTO> list) {

        myStockService.addBatch(user.getUsername(), list);
        return Map.of("result", "SUCCESS");
    }

    /**
     * 관심종목 목록 조회 (KR / US 동시)
     */
    @GetMapping("/list")
    public Map<String, Object> myStockList(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") int krPage,
            @RequestParam(defaultValue = "1") int usPage) {

        PageRequestDTO krPageRequest = PageRequestDTO.builder()
                .page(krPage)
                .size(10)
                .build();

        PageRequestDTO usPageRequest = PageRequestDTO.builder()
                .page(usPage)
                .size(10)
                .build();

        PageResponseDTO<MyStockDTO> krResult =
                myStockService.getMyStockListKR(user.getUsername(), krPageRequest);

        PageResponseDTO<MyStockDTO> usResult =
                myStockService.getMyStockListUS(user.getUsername(), usPageRequest);

        return Map.of(
                "kr", krResult,
                "us", usResult
        );
    }

    /**
     * 관심종목 단일 삭제 (soft delete)
     */
    @PostMapping("/delete/{id}")
    public Map<String, Object> deleteMyStock(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        myStockService.delete(id, user.getUsername());
        return Map.of("result", "SUCCESS");
    }

    /**
     * 삭제된 관심종목 목록
     */
    @GetMapping("/deleted")
    public PageResponseDTO<MyStockDTO> deletedList(
            @AuthenticationPrincipal User user,
            PageRequestDTO pageRequestDTO) {

        return myStockService.getDeletedListPaging(
                user.getUsername(), pageRequestDTO
        );
    }

    /**
     * 관심종목 복구
     */
    @PostMapping("/restore/{id}")
    public Map<String, Object> restore(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        myStockService.restore(id, user.getUsername());
        return Map.of("result", "SUCCESS");
    }

    /**
     * 관심종목 KR 목록
     */
    @GetMapping("/list/kr")
    public PageResponseDTO<MyStockDTO> krList(
            @AuthenticationPrincipal User user,
            PageRequestDTO pageRequestDTO) {

        return myStockService.getMyStockListKR(
                user.getUsername(), pageRequestDTO
        );
    }

    /**
     * 관심종목 US 목록
     */
    @GetMapping("/list/us")
    public PageResponseDTO<MyStockDTO> usList(
            @AuthenticationPrincipal User user,
            PageRequestDTO pageRequestDTO) {

        return myStockService.getMyStockListUS(
                user.getUsername(), pageRequestDTO
        );
    }
}
