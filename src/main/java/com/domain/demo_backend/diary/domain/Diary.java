package com.domain.demo_backend.diary.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class Diary {

    private String title;
    private String content;
    private String tag1;
    private String tag2;
    private String tag3;
    private String date;
    private String userId;
    private String username;
    private BigInteger userSqno;
    private String sbsceDt;
    private String lastUpdtDt;
    private String roleCd;
    private String roleNm;
    private LocalDateTime regDt;
    private LocalDateTime updtDt;
    private String diaryStatus;
    private String diaryType;
    private String delYn;
    private LocalDateTime delDt;
    private LocalDateTime frstRegDt;
    private String frstRegIp;
    private String lastUpdtIp;
    private Long frstRgstUspsSqno;
    private Long lastUpdtUspsSqno;

    public Diary toDiary() {
        return Diary.builder()
                .userId(this.userId)
                .title(this.title)
                .content(this.content)
                .tag1(this.tag1)
                .tag2(this.tag2)
                .tag3(this.tag3)
                .diaryStatus(this.diaryStatus)
                .build();
    }

    @Builder
    public Diary(String title, String content,String tag1,String tag2,String tag3, String date, String userId, String username, BigInteger userSqno, String sbsceDt, String lastUpdtDt, String roleCd, String roleNm, LocalDateTime regDt, LocalDateTime updtDt, String diaryStatus, String diaryType, String delYn, LocalDateTime delDt, LocalDateTime frstRegDt, String frstRegIp, String lastUpdtIp, Long frstRgstUspsSqno, Long lastUpdtUspsSqno) {
        this.title = title;
        this.content = content;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.date = date;
        this.userId = userId;
        this.username = username;
        this.userSqno = userSqno;
        this.sbsceDt = sbsceDt;
        this.lastUpdtDt = lastUpdtDt;
        this.diaryStatus = diaryStatus;
    }

}
