package com.domain.demo_backend.diary.dto;


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

    private BigInteger userSqno;
    private String userId;
    private String title;
    private String content;
    private String date;
    private String username;
    private LocalDate sbsceDt;


    // 최종 수정 일시
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastUpdtDt;
}
