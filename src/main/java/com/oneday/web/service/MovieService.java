package com.oneday.web.service;

import com.oneday.web.dto.MovieDTO;
import com.oneday.web.dto.MovieImageDTO;
import com.oneday.web.entity.Movie;
import com.oneday.web.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {
    Long register(MovieDTO movieDTO);

    // Movie + MovieImage 객체 처리 위해 Map 타입 변환
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        // MovieImageDTO 처리
        if (imageDTOList != null && imageDTOList.size() > 0) {
            List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
                MovieImage movieImage = MovieImage.builder()
                                            .path(movieImageDTO.getPath())
                                            .imgName(movieImageDTO.getImgName())
                                            .uuid(movieImageDTO.getUuid())
                                            .movie(movie)
                                            .build();

                return movieImage;
            }).collect(Collectors.toList());

            entityMap.put("imgList", movieImageList);
        }

        return entityMap;
    }
}
