package com.domain.demo_backend.diary.dto;

import com.domain.demo_backend.diary.domain.Diary;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigInteger;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DiaryRequest {

    //현재 페이지 번호
    private int pageNo = 1;
    // 페이지 크기
    private int pageSize = 10;
    private String diaryId;
    // 검색 타입(ID, Name, Title)
    private String searchType;
    // 검색어
    private String searchText;

    private String diaryStatus;
    // 사용자 일련번호
     private BigInteger userSqno;
     // 글쓴이
    private String userId;
     // 최초 등록 IP
    private String frstRegstIp;

     // 최중 수정 IP
    private String lastUpdtIp;

    // 최종 수정 일시
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String lastUpdtDt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String regDt;
    // 삭제 여부
    private String delYn;

    private String title;

    private String content;

    // 테그1, 테그2, 테그3
    private String tag1;
    private String tag2;
    private String tag3;

    private String author;
    private Integer emotion;
    public Diary toDiary() {
        return Diary.builder()
                .diaryId(this.diaryId)
                .userSqno(this.userSqno)
                .userId(this.userId)
                .title(this.title)
                .content(this.content)
                .tag1(this.tag1)
                .tag2(this.tag2)
                .tag3(this.tag3)
                .diaryStatus(this.diaryStatus)
                .author(this.author)
                .emotion(this.emotion)
                .build();
    }

}
