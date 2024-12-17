package com.domain.demo_backend.mapper;

import com.domain.demo_backend.diary.domain.Diary;
import com.domain.demo_backend.diary.dto.DiaryRequest;
import com.domain.demo_backend.diary.dto.DiaryResponse;

import java.util.List;
import java.util.Set;

public interface DiaryMapper {
    void insertDiary(Diary diary);

    void updateDiaryDel(Set<Diary> diaryRemoveList, Diary diary);

    void updateDiarMnpsDel(Set<Diary> diaryRemoveList, Diary diary);

    List<DiaryResponse> selectDiaryList(DiaryRequest diaryReq);
}
