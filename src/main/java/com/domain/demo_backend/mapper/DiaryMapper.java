package com.domain.demo_backend.mapper;

import com.domain.demo_backend.diary.domain.Diary;
import com.domain.demo_backend.diary.dto.DiaryRequest;
import com.domain.demo_backend.diary.dto.DiaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DiaryMapper {
    void insertDiary(Diary diary);

    void updateDiaryDel(Set<Diary> diaryRemoveList, Diary diary);

    List<DiaryResponse> selectDiaryList(@Param("userId") String userId);

    void updateDiarMnpsDel(Set<Diary> diaryRemoveList, Diary diary);

    Set<DiaryResponse> findDiaryItemById(String diaryReq);

    Set<DiaryResponse> selectDiaryItem(DiaryRequest diaryReq);
}
