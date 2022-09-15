package com.oneday.web.service;

import com.oneday.web.dto.MovieDTO;
import com.oneday.web.entity.Movie;
import com.oneday.web.entity.MovieImage;
import com.oneday.web.repository.MovieImageRepository;
import com.oneday.web.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    private final MovieImageRepository movieImageRepository;

    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {
        // DTO -> Entity 변환
        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        Movie movie = (Movie)entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);

        // 이미지 처리
        movieImageList.forEach(movieImage -> {
            movieImageRepository.save(movieImage);
        });


        return movie.getMno();
    }
}
