package com.domain.demo_backend.diary.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String userSqno;
    private String sbsceDt;
    private String lastUpdtDt;
    private String RoleCd;
    private String RoleNm;
    private LocalDateTime regDt;
    private LocalDateTime updtDt;
    private String DiaryStatus;
    private String DiaryType;
    private String delYn;
    private LocalDateTime delDt;
    private LocalDateTime frstRegDt;
    private String frstRegIp;
    private String lastUpdtIp;
    private Long frstRgstUspsSqno;
    private Long lastUpdtUspsSqno;

    @Builder
    public Diary(String title, String content,String tag1,String tag2,String tag3, String date, String userId, String username, String userSqno, String sbsceDt, String lastUpdtDt, String RoleCd, String RoleNm, LocalDateTime regDt, LocalDateTime updtDt, String DiaryStatus, String DiaryType, String delYn, LocalDateTime delDt, LocalDateTime frstRegDt, String frstRegIp, String lastUpdtIp, Long frstRgstUspsSqno, Long lastUpdtUspsSqno) {
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
    }

}
