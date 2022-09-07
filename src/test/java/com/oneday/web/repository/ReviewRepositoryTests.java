package com.oneday.web.repository;

import com.oneday.web.entity.Member2;
import com.oneday.web.entity.Movie;
import com.oneday.web.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertReview() {
        IntStream.rangeClosed(1, 200).forEach(i -> {
            // 영화 번호
            Long mno = (long)(Math.random() * 100) + 1;

            // 리뷰어 번호
            Long mid = ((long)(Math.random() * 100) + 1);
            Member2 member = Member2.builder().mid(mid).build();

            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random() * 5) + 1)
                    .text("영화 소감은.." + i)
                    .build();

            reviewRepository.save(movieReview);

        });
    }

    @Test
    public void testGetMovieReviews() {
        Movie movie = Movie.builder().mno(Long.valueOf(14)).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(i -> {
            System.out.println(i.getReviewnum());
            System.out.println("\t" + i.getGrade());
            System.out.println("\t" + i.getText());
            System.out.println("\t" + i.getMember().getEmail());
            System.out.println("-----------------------------");
        });
    }
}
