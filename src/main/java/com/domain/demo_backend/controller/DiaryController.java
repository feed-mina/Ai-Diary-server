package com.domain.demo_backend.controller;

import com.domain.demo_backend.diary.domain.Diary;
import com.domain.demo_backend.diary.dto.DiaryRequest;
import com.domain.demo_backend.diary.dto.DiaryResponse;
import com.domain.demo_backend.helper.UserInfoHelper;
import com.domain.demo_backend.service.DiaryService;
import com.domain.demo_backend.user.domain.User;
import com.domain.demo_backend.util.ApiResponseDto;
import com.domain.demo_backend.util.CustomUserDetails;
import com.github.pagehelper.PageInfo;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {
    @Autowired
    private  DiaryService diaryService;

    private Authentication authentication;

      @PostMapping("/addDiaryList")
      @ResponseBody
      public ResponseEntity<String> addDiaryList(@RequestBody  DiaryRequest diaryRequest, @RequestHeader("X-Forwarded-For") String ip) {

          System.out.println("addDiaryList진입, diaryReq값: " + diaryRequest);

          // 현재 로그인된 사용자 ID 가져오기
          CustomUserDetails currentUser = UserInfoHelper.getMemberInfo();
          BigInteger currentUserSqno = currentUser.getUserSqno();

          String currentUserName = currentUser.getUsername();
          BigInteger currentUserId = currentUser.getUserSqno();
          String currentUserId2 = String.valueOf(UserInfoHelper.getMemberInfo().getUserSqno());

          System.out.println("현재 로그인된 사용자: userSqno=" + currentUserSqno + ", username=" + currentUserId);

          System.out.println("diaryService 들어가기" +diaryRequest);
          System.out.println("currentUserId: " + currentUserId);
          try {
              if (diaryRequest.getUserId() == null || !diaryRequest.getUserId().equals(currentUserId)) {
                  throw new IllegalArgumentException("로그인한 유저만 자신의 일기를 작성할 수 있습니다.");
              }
              diaryService.addDiary(diaryRequest, ip, (Authentication) SecurityContextHolder.getContext()); // 단일 Diary 삽입

          } catch (IllegalArgumentException e) {
              System.err.println("Invalid request: " + e.getMessage());

              return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 요청입니다");
          } catch (Exception e) {
              System.err.println("서버 오류: " + e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
          }
          return ResponseEntity.ok("Diary added successfully.");

      }

@GetMapping("/viewDiaryList")
    public String viewDiaryList(DiaryRequest diaryReq, Model model) {
    PageInfo<DiaryResponse> diaryList = diaryService.selectDiaryList(diaryReq);
    model.addAttribute("diaryList", diaryList);
    model.addAttribute("diaryReq", diaryReq);
    model.addAttribute("diaryListSize", diaryList.getTotal());
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
