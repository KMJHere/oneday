package com.oneday.web.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 제네릭 타입 이용
@Data
public class PageResultDTO<DTO, EN> {
    // DTO리스트
    private List<DTO> dtoList;

    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    // 이전, 다음
    private boolean prev, next;

    // 페이지 번호 목록
    private List<Integer> pageList;

    // Function<EN, DTO> => 엔티티 객체들을 DTO로 변환
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        // Collectors 이용하여 stream -> List 타입으로 리턴
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // 페이지 번호는 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        int tempEnd = (int)Math.ceil(page/10.0) * 10;

        start = tempEnd - 9;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        prev = start > 1;

        next = totalPage > tempEnd;

        // Intstream > boxed() 클래스 타입으로 전환
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
