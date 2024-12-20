package com.domain.demo_backend.service;

import com.domain.demo_backend.diary.domain.Diary;
import com.domain.demo_backend.diary.dto.DiaryRequest;
import com.domain.demo_backend.diary.dto.DiaryResponse;
import com.domain.demo_backend.mapper.DiaryMapper;
import com.domain.demo_backend.util.ApiResponseCode;
import com.domain.demo_backend.util.ApiResponseDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
        PageHelper.startPage(diaryReq.getPageNo(), diaryReq.getPageSize());
        List<DiaryResponse> diaryResponseList = null;
        try {
            diaryResponseList = diaryMapper.selectDiaryList(diaryReq);
        } catch (Exception e) {
            System.err.println("Error fetching diary list: " + e.getMessage());
            throw new RuntimeException("일기를 조회하는 도중 오류가 발생했습니다.", e);
        }

        // PageInfo 객체로 페이징 결과를 반환
        return new PageInfo<>(diaryResponseList);
    }

    public void addDiary(Diary diary){
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
            Map<String, List<DiaryResponse>> diaryRequestMap = list.stream()
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
