package com.oneday.web.service;

import com.oneday.web.dto.ReviewDTO;
import com.oneday.web.entity.Member;
import com.oneday.web.entity.Member2;
import com.oneday.web.entity.Movie;
import com.oneday.web.entity.Review;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewService {
    // 영화의 모든 리뷰 가져오기
    List<ReviewDTO> getListOfMovie(Long mno);

    // 영화 리뷰를 추가
    Long register(ReviewDTO movieReviewDTO);

    // 특정 영화리뷰 수정
    void modify(ReviewDTO movieReviewDTO);

    // 영화 리뷰 삭제
    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewDTO movieReviewDTO) {
        Review movieReview = Review.builder()
                .reviewnum(movieReviewDTO.getReviewnum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member2.builder().mid(movieReviewDTO.getMid()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();

        return movieReview;
    }

    default ReviewDTO entityToDTO(Review review) {
        ReviewDTO reviewDTO = ReviewDTO.builder()
                    .reviewnum(review.getReviewnum())
                    .mno(review.getMovie().getMno())
                    .mid(review.getMember().getMid())
                    .nickname(review.getMember().getNickname())
                    .email(review.getMember().getEmail())
                    .grade(review.getGrade())
                    .grade(review.getGrade())
                    .text(review.getText())
                    .regDate(review.getRegDate())
                    .modDate(review.getModDate())
                .build();

        return reviewDTO;
    }
}
