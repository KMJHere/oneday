package com.oneday.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "m_member")
public class Member2 extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;

    private String email;

    private String pw;

    private String nickname;
}
