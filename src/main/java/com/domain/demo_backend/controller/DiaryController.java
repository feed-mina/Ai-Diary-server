package com.domain.demo_backend.controller;

import com.domain.demo_backend.diary.domain.Diary;
import com.domain.demo_backend.diary.dto.DiaryRequest;
import com.domain.demo_backend.diary.dto.DiaryResponse;
import com.domain.demo_backend.helper.UserInfoHelper;
import com.domain.demo_backend.service.DiaryService;
import com.domain.demo_backend.user.domain.User;
import com.domain.demo_backend.util.ApiResponseDto;
import com.domain.demo_backend.util.CustomUserDetails;
import com.domain.demo_backend.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {
    private final DiaryService diaryService;

    private final JwtUtil jwtUtil;

    @Autowired
    public DiaryController(DiaryService diaryService, JwtUtil jwtUtil) {
        this.diaryService = diaryService;
        this.jwtUtil = jwtUtil;
    }

      @PostMapping("/addDiaryList")
      @ResponseBody
      public ResponseEntity<String> addDiaryList(HttpServletRequest request, HttpServletResponse response,  @RequestBody  DiaryRequest diaryRequest, @RequestHeader("X-Forwarded-For") String ip) {
          System.out.println("addDiaryList진입, diaryReq값: " + diaryRequest);

          String authorizationHeader = request.getHeader("Authorization");
          System.out.println("Header: " + authorizationHeader);

          if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid.");
          }

          String token = authorizationHeader.substring(7);
          System.out.println("token : " + token);
          Claims claims;
          try {
              claims = jwtUtil.validateToken(token);
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT Token.");
          }
          String userSqnoStr = claims.get("userSqno", String.class); // String으로 읽기
          BigInteger currentUserSqno = new BigInteger(userSqnoStr);
          System.out.println("currentUserSqno 값: " + currentUserSqno);

          // 현재 로그인된 사용자 ID 가져오기 er
          CustomUserDetails currentUser = UserInfoHelper.getMemberInfo();
          System.out.println("currentUser 값: " + currentUser);

          String currentUserName = currentUser.getUsername();
          String currentUserId = currentUser.getUserId();
          System.out.println("현재 로그인된 사용자: userSqno=" + currentUserSqno );
          System.out.println("diaryService 들어가기" +diaryRequest);
          try {
              if (diaryRequest.getUserId() == null || !diaryRequest.getUserId().equals(currentUserId)) {
                  System.out.println("diaryRequest.getUserId() : " + diaryRequest.getUserId());
                  throw new IllegalArgumentException("로그인한 유저만 자신의 일기를 작성할 수 있습니다.");
              }
          } catch (IllegalArgumentException e) {
            System.err.println("Invalid request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 요청입니다");
            } catch (Exception e) {
              System.err.println("서버 오류: " + e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
          }
          diaryService.addDiary(diaryRequest, ip, SecurityContextHolder.getContext().getAuthentication());

          return ResponseEntity.ok().body(Map.of("success", true).toString());
      }

@GetMapping("/viewDiaryList")
    public String viewDiaryList(DiaryRequest diaryReq, Model model) {
        System.out.println("diaryReq: " +diaryReq );
        System.out.println("selectDiaryList 다이어리 서비스 로직 진입");
    PageInfo<DiaryResponse> diaryList = diaryService.selectDiaryList(diaryReq);
    System.out.println("diaryList: " +diaryList );
    model.addAttribute("diaryList", diaryList);
    model.addAttribute("diaryReq", diaryReq);
    model.addAttribute("diaryListSize", diaryList.getTotal());

    System.out.println("diaryList-model: " +model );
    return "viewDiaryList";
}

@GetMapping("/findDiarySearchList")
@ResponseBody
    public ApiResponseDto<List<DiaryResponse>>  findDiarySearchList(DiaryRequest diaryReq) {
    // 현재 사용자의 고유번호를 가져옴
    BigInteger currentUserId = UserInfoHelper.getMemberInfo().getUserSqno();
    // 요청 객체에 사용자 고유 번호를 세팅
        diaryReq.setUserSqno(currentUserId);
    return ApiResponseDto.success((List<DiaryResponse>) diaryService.findDiaryList(diaryReq));

}

}
