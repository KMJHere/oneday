package com.oneday.web.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardDTO {
    private Long bno;

    private String title;

    private String content;

    private String writerEmail; // 작성자 이메일(id)

    private String writerName; // 작성자 이름

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private int replyCnt; // 게시글별 댓글수
}
