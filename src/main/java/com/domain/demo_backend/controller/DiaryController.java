package com.domain.demo_backend.controller;

import com.domain.demo_backend.diary.domain.Diary;
import com.domain.demo_backend.diary.dto.DiaryRequest;
import com.domain.demo_backend.diary.dto.DiaryResponse;
import com.domain.demo_backend.helper.UserInfoHelper;
import com.domain.demo_backend.service.DiaryService;
import com.domain.demo_backend.util.ApiResponseDto;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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

      @PostMapping("/addDiaryList")
      @ResponseBody
      public ResponseEntity<Diary> addDiaryList(@RequestBody  DiaryRequest diaryReq, @RequestHeader("X-Forwarded-For") String ip) {
          BigInteger currentUserSqno = UserInfoHelper.getMemberInfo().getUserSqno();

          // Diary 객체 생성
          Diary diary = Diary.builder()
                  .userSqno(String.valueOf(diaryReq.getUserSqno()))
                  .frstRegIp(ip)
                  .content(diaryReq.getContent())
                  .title(diaryReq.getTitle())
                  .build();
          try {
              diaryService.addDiary(diary); // 단일 Diary 삽입

          } catch (Exception e) {
              if(!(diaryReq.getUserSqno().equals(currentUserSqno))){
                  throw new IllegalArgumentException("로그인한 유저는 자신의 일기만 작성할 수 있습니다.");
              }
          }
          return ResponseEntity.ok(diary);
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
    BigInteger currentUserSqno = UserInfoHelper.getMemberInfo().getUserSqno();
    // 요청 객체에 사용자 고유 번호를 세팅
        diaryReq.setUserSqno(Long.valueOf(currentUserSqno.toString()));
    return ApiResponseDto.success((List<DiaryResponse>) diaryService.findDiaryList(diaryReq));

}

}
