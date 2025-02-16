package com.domain.demo_backend.controller;

import com.domain.demo_backend.service.AuthService;
import com.domain.demo_backend.user.dto.EmailRequest;
import com.domain.demo_backend.user.dto.LoginRequest;
import com.domain.demo_backend.user.dto.LoginResponse;
import com.domain.demo_backend.user.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name="회원 권한 로직 컨트롤러", description = "로그인, 회원가입 (-- 로그아웃, 회원탈퇴, kakao OAuth2추가 예정 --)")
public class AuthController {
    private Map<String, String> emailVerificationMap = new HashMap<>();

    private final AuthService authService;

    // 생성자 주입
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    @Operation(summary = "회원 로그인" , description = "id와 password와 haspassword가 일치하다면 로그인, 아니면 팝업 경고창이 뜬다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일반 회원 로그인 성공"),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치"),
            @ApiResponse(responseCode = "403", description = "계정 비 활성화 또는 회원탈퇴"),
            @ApiResponse(responseCode = "500", description = "서버오류"),
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try{
            String jwt = authService.login(loginRequest);

            System.out.println("로그인 성공");
            return ResponseEntity.ok(new LoginResponse(jwt));
         }catch (RuntimeException e){
            // 명확한 에러 메시지 반환
            Map<String, String> errorResponse = new HashMap<>();

            System.out.println("로그인 실패");
            errorResponse.put("error", "로그인 실패");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "회원 가입" , description = "새로운 회원을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자"),
            @ApiResponse(responseCode = "500", description = "서버오류"),
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("회원가입 진입");

        System.out.println("registerRequest: "+ registerRequest);
        authService.register(registerRequest);
        System.out.println("register service logic OK");
        return ResponseEntity.ok("User registred successfully!");
    }

    @Operation(summary = "회원 탈퇴" , description = "사용자 계정의 del_yn flag를 'Y' -> 'N'로 표시한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
            @ApiResponse(responseCode = "500", description = "서버오류"),
    })
    @PostMapping("/non-user")
    public ResponseEntity<String> nonUser(@RequestBody RegisterRequest registerRequest) {
        System.out.println("회원탈퇴 요청 진입: " + registerRequest);
        System.out.println("회원탈퇴 진입");
        if (registerRequest.getUserId() == null || registerRequest.getUserId().isEmpty()) {
            System.out.println("회원탈퇴 실패: userId가 비어 있음");
            return ResponseEntity.badRequest().body("회원 아이디가 필요합니다.");
        }

        try {
            authService.nonMember(registerRequest);
            return ResponseEntity.ok("회원탈퇴 성공");
        } catch (IllegalArgumentException e) {
            System.out.println("회원탈퇴 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("회원탈퇴 실패: 서버 내부 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }

    }

    @Operation(summary = "Kakao OAuth2 로그인" , description = "Kakao 계정으로 로그인 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 로그인 성공"),
            @ApiResponse(responseCode = "400", description = "카카오 인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버오류"),
    })
    @GetMapping("/oauth2/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {
        // 카카오 OAuth2 로그인 로직
        return ResponseEntity.ok("Kakao 로그인 성공");
    }

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        authService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        return "Email sent successfully";
    }

    @PostMapping("/send-html")
    public String sendHtmlEmail(@RequestBody EmailRequest emailRequest) {
        try {
            authService.sendHtmlEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody(),emailRequest.getImagePath());
            return "HTML Email sent successfully!";
        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

    @PostMapping("/SignUp")
    public String sendVerificationCode(@RequestParam String email, @RequestParam String message) throws MessagingException {
        // 랜덤 인증 코드 생성
        // String verificationCode = generateVerification();
        String verificationCode = authService.sendVerificationCode(email);
        // 인증 코드를 Map에 저장
        emailVerificationMap.put(email, verificationCode);

        // 이메일 전송 시뮬레이션(실제 서비스에서는 이메일 전송 API)
        System.out.println("이메일 전송: " + email);
        System.out.println("메시지: " + message + " 코드 : ");

        String savedCode = emailVerificationMap.get(email);
        return "인증 코드가 다음 이메일로 전송되었습니다." + email;
    }
    @PostMapping("/verify")
    public String verifyCode(@RequestParam String email, @RequestParam String code) {
        String saveCode = emailVerificationMap.get(email);
        if( saveCode != null && saveCode.equals(code)){
            emailVerificationMap.remove(email); // 인증 성공 시 코드 제거
            return "이메일 인증 성공!";
        }else {
            return "인증 실패 : 잘못된 코드입니다.";
        }
    }
}
