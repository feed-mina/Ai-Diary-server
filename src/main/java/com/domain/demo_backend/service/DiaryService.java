package com.domain.demo_backend.service;

import com.domain.demo_backend.diary.domain.Diary;
import com.domain.demo_backend.diary.dto.DiaryRequest;
import com.domain.demo_backend.diary.dto.DiaryResponse;
import com.domain.demo_backend.mapper.DiaryMapper;
import com.domain.demo_backend.util.ApiResponseCode;
import com.domain.demo_backend.util.ApiResponseDto;
import com.domain.demo_backend.util.CustomUserDetails;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DiaryService {
    @Autowired
    private DiaryMapper diaryMapper;

    public DiaryService(DiaryMapper diaryMapper) {
        this.diaryMapper = diaryMapper;
    }

    public PageInfo<DiaryResponse> selectDiaryList(DiaryRequest diaryReq) {
        System.out.println("다이어리 서비스 selectDiaryList진입");
        PageHelper.startPage(diaryReq.getPageNo(), diaryReq.getPageSize());
        List<DiaryResponse> diaryResponseList = null;
        try {
            diaryResponseList = diaryMapper.selectDiaryList(diaryReq);

            System.out.println("diaryResponseList: "+ diaryResponseList);
        } catch (Exception e) {
            System.err.println("Error fetching diary list: " + e.getMessage());
            throw new RuntimeException("일기를 조회하는 도중 오류가 발생했습니다.", e);
        }

        // PageInfo 객체로 페이징 결과를 반환
        return new PageInfo<>(diaryResponseList);
    }

    public void addDiary(DiaryRequest diaryRequest, String ip, Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println("userDetails-다이어리서비스: " + userDetails);
        diaryRequest.setUserSqno(userDetails.getUserSqno());

        // 로깅을 위해 추가
        System.out.println("diaryRequest-다이어리서비스: " +diaryRequest);
        System.out.println("diaryRequest.toDiary()-다이어리서비스: " +diaryRequest.toDiary());
        Diary diary = Diary.builder()
                .userSqno(diaryRequest.getUserSqno() != null ? diaryRequest.getUserSqno() : userDetails.getUserSqno())
                .title(diaryRequest.getTitle() != null ? diaryRequest.getTitle() : "Untitled")
                .content(diaryRequest.getContent() != null ? diaryRequest.getContent() : "")
                .tag1(diaryRequest.getTag1() != null ? diaryRequest.getTag1() : "")
                .tag2(diaryRequest.getTag2() != null ? diaryRequest.getTag2() : "")
                .tag3(diaryRequest.getTag3() != null ? diaryRequest.getTag3() : "")
                .diaryStatus(diaryRequest.getDiaryStatus() != null? diaryRequest.getDiaryStatus() : false)
                .frstRegIp(ip != null ? ip : "127.0.0.1")
                .frstRgstUspsSqno(userDetails.getUserSqno() != null ? userDetails.getUserSqno() : BigInteger.ZERO)
                .author(diaryRequest.getAuthor() != null ? diaryRequest.getAuthor() : "")
                .emotion(diaryRequest.getEmotion() != null ? diaryRequest.getEmotion() : 0)
                .build();


        System.out.println("Diary 객체 생성 값: " + diary);

        diaryMapper.insertDiary(diary);
    }

    public void updateDiaryDel(Set<Diary> diaryRemoveList, Diary diary){
        diaryMapper.updateDiaryDel(diaryRemoveList, diary);
        diaryMapper.updateDiarMnpsDel(diaryRemoveList, diary);
    }

    public ApiResponseDto<String> createDiary(){
        // 기본 응답 초기화
        ApiResponseDto<String> response = ApiResponseCode.DEFAULT_OK;
        try{
            // 데이터베이스에서 객체 리스트를 가져온다.
            List<DiaryResponse> list = diaryMapper.selectDiaryList(new DiaryRequest());
            // 리스트를 userSqno(고유번호)로 그룹화한다.
            Map<BigInteger, List<DiaryResponse>> diaryRequestMap = list.stream()
                    .sorted(Comparator.comparing(DiaryResponse::getUserSqno))
                    .collect(Collectors.groupingBy(DiaryResponse::getUserSqno));

            // 결과 리스트 처리
            List<DiaryResponse> diaryResponseList = new ArrayList<>();
            System.out.println("Diary Request Map : "+diaryRequestMap);
        } catch (Exception e) {
            response = ApiResponseDto.error("Internal Server Error");
            throw new RuntimeException("Diary creatuib failed",e);
        }
        return response;
    }

    public Object findDiaryList(DiaryRequest diaryReq) {
        return diaryMapper.selectDiaryList(diaryReq);
    }
}
