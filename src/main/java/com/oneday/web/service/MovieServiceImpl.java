package com.oneday.web.service;

import com.oneday.web.dto.MovieDTO;
import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.dto.PageResultDTO;
import com.oneday.web.entity.Movie;
import com.oneday.web.entity.MovieImage;
import com.oneday.web.repository.MovieImageRepository;
import com.oneday.web.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());
        Page<Object[]> result = movieRepository.getListPage(pageable);

        // Function<T, R> T-입력유형/R-결과유형
        Function<Object[], MovieDTO> fn = (
                arr -> entitiesToDTO(
                            (Movie)arr[0],
                            (List<MovieImage>)(Arrays.asList((MovieImage)arr[1])),
                            (Double)arr[2],
                            (Long)arr[3]
                        )
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MovieDTO getMovie(Long mno) {
        // getMovieWithAll > List<Object[]> 반환
        List<Object[]> result = movieRepository.getMovieWithAll(mno);
        
        Movie movie = (Movie) result.get(0)[0]; // Movie entity 중 첫번째 데이터, image 데이터로 인해 중복되는 로우가 있어도 movie, avg, reviewCnt는 동일

        List<MovieImage> movieImageList = new ArrayList<>(); // 목록과 달리 영화의 이미지 개수만큼 MovieImage 객체 필요

        result.forEach(arDat -> {
            // Image 필드 index = 1
            MovieImage movieImage = (MovieImage)arDat[1];
            movieImageList.add(movieImage);
        });

        // avg 필드 index = 2
        Double avg = (Double)result.get(0)[2];
        Long reviewCnt = (Long)result.get(0)[3];

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }
}
