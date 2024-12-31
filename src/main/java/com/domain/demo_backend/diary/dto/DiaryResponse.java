package com.domain.demo_backend.diary.dto;


import com.domain.demo_backend.diary.domain.Diary;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public  class DiaryResponse {
    private BigInteger diaryId;
    private BigInteger userSqno;
    private String userId;
    private String author;
    private String title;
    private String content;
    private String tag1;
    private String tag2;
    private String tag3;
    private String username;
    private Boolean diaryStatus;
    private Integer emotion;
    private String delYn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;


    // 최종 수정 일시
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDt;

    // 최종 수정 일시
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastUpdtDt;

    public Diary toDiary() {
        return Diary.builder()
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
                .regDt(this.regDt)
                .build();
    }
}
