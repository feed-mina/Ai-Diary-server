package com.domain.demo_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/") // 기본 URL (localhost:8080)로 접속 시 실행
    public String indexPage() {
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

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
