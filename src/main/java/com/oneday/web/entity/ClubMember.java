package com.oneday.web.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity {
    @Id
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;
}
