package com.oneday.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString(exclude = "board")
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;
    
    private String replyer;
    
    @ManyToOne
    private Board board; //연관관계 지정
}
