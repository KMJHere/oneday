package com.oneday.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString(exclude = "movie")
@NoArgsConstructor
@AllArgsConstructor
public class MovieImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    @ManyToOne
    private Movie movie;
}
