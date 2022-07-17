package com.oneday.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString(exclude = "writer") // @ToString은 항상 exclude
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    
    private String title;
    
    private String content;
    
    @ManyToOne
    private Member writer; // 연관관계 지정
}
