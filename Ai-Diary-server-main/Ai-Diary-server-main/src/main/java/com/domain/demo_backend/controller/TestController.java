package com.domain.demo_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name="swagger 테스트", description = "swagger API 예제 문서")
public class TestController {

    @Operation(summary="기본 default url 설정", description = "localhost:8080들어가면 로그인 페이지로 리다이렉트 하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 페이지로 redirect")
    })
    @GetMapping("/") // 기본 URL (localhost:8080)로 접속 시 실행
    public String indexPage() {
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

    @Operation(summary = "주소창에 /login 으로 바로 들어가는 경우 또는 회원가입 후 로그인 페이지로 가는 경우",description = "get방식으로 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 페이지")
    })
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html 뷰 반환
    }

    @GetMapping("/home")
    public String homePage() {
        return "home"; // home.html 뷰 반환
    }

    // vue api 테스트 셈플
    @GetMapping("/api/user")
    public String getUserInfo() {
        return "{\"name\": \"홍길동\", \"age\": 25}";
    }

    /* vue에서 사용방법
    *     axios
      .get("http://localhost:8080/api/user") // 스프링부트 API 주소
      .then((response) => {
        this.user = response.data; // API 응답 데이터를 저장
      })
      .catch((error) => {
        console.error("API 호출 실패:", error);
      });

      * */
}
