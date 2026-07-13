package com.stock.webpage.api;

import com.stock.webpage.dto.MemberDTO;
import com.stock.webpage.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

/**
 * 관리자 전용 기능을 처리하는 REST 컨트롤러 클래스입니다.
 * 모든 API 경로는 /api/admin으로 시작하며, 시큐리티 설정을 통해 관리자(ADMIN) 권한을 가진 인증 사용자만 접근할 수 있습니다.
 */
@Log4j2
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ApiAdminController {

    private final MemberService memberService;

    @Value("${batch.csv.dir}")
    private String uploadDir;

    /**
     * 관리자가 시스템의 모든 회원 목록을 조회하는 API 엔드포인트입니다.
     *
     * @return 전체 회원 목록 데이터 리스트
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> listMembers() {
        log.info("관리자 권한 회원 전체 조회 API 호출");
        List<MemberDTO> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    /**
     * 관리자가 특정 회원의 등급(grade) 정보를 수정하는 API 엔드포인트입니다.
     *
     * @param mid   수정 대상 회원의 아이디
     * @param param 변경할 등급(grade) 정보가 포함된 key-value 객체
     * @return 등급 수정 처리 결과 응답
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/members/{mid}/grade")
    public ResponseEntity<?> updateGrade(
            @PathVariable String mid,
            @RequestBody Map<String, String> param
    ) {
        String grade = param.get("grade");
        log.info("관리자 권한 회원 등급 변경 API 호출 - 아이디: {}, 변경 등급: {}", mid, grade);

        if (grade == null || grade.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "result", "FAIL",
                    "reason", "MISSING_GRADE_PARAMETER"
            ));
        }

        try {
            memberService.modifyMemberGrade(mid, grade);
            return ResponseEntity.ok(Map.of("result", "OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "result", "FAIL",
                    "reason", "INVALID_GRADE",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * 관리자가 배치 연동을 위해 지정된 유형의 CSV 파일을 업로드하는 API 엔드포인트입니다.
     * 지정 폴더(/workspace/BATCH_CODE/csvDir)가 부재하면 디렉토리를 신설하며, 기존 파일명이 있으면 덮어씁니다.
     *
     * @param file     업로드 대상 파일
     * @param fileType 파일 저장 유형 구분자
     * @return 파일 저장 처리 결과
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCsvFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType
    ) {
        log.info("관리자 CSV 파일 업로드 요청 - 파일 유형: {}, 원본 파일명: {}", fileType, file.getOriginalFilename());

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "result", "FAIL",
                    "message", "전송된 파일이 비어 있습니다."
            ));
        }

        String targetFileName;
        switch (fileType) {
            case "MONTHLY_ETF":
                targetFileName = "KOREA_ETF_INFO.csv";
                break;
            case "MONTHLY_KRX":
                targetFileName = "KOREA_COMPANY_INFO.csv";
                break;
            case "MONTHLY_NPS_KR":
                targetFileName = "NPS_INFO_KR.csv";
                break;
            case "MONTHLY_NPS_US":
                targetFileName = "NPS_INFO_US.csv";
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of(
                        "result", "FAIL",
                        "message", "올바르지 않은 파일 유형 구분자 파라미터입니다."
                ));
        }

        try {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("CSV 업로드 저장소 폴더 신설 완료: {}", uploadDir);
            }

            Path targetFilePath = path.resolve(targetFileName);
            Files.copy(
                    file.getInputStream(),
                    targetFilePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.info("관리자 CSV 파일 덮어쓰기 저장 완료: {}", targetFilePath.toAbsolutePath());

            return ResponseEntity.ok(Map.of(
                    "result", "OK",
                    "fileName", targetFileName,
                    "savedPath", targetFilePath.toAbsolutePath().toString()
            ));

        } catch (Exception e) {
            log.error("관리자 CSV 파일 업로드 실패: ", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "result", "FAIL",
                    "message", "서버 저장 오류: " + e.getMessage()
            ));
        }
    }
}
