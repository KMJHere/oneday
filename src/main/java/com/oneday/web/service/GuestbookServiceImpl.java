package com.oneday.web.service;

import com.oneday.web.controller.GuestbookController;
import com.oneday.web.dto.GuestbookDTO;
import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.dto.PageResultDTO;
import com.oneday.web.entity.Guestbook;
import com.oneday.web.entity.QGuestbook;
import com.oneday.web.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.DTD;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository; // 반드시 final로 선언

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 처리

        // Querydsl 사용
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);

        // java.util.function 생성
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);

        return result.isPresent() ? entityToDTO(result.get()): null;
    }

    // 등록 
    @Override
    public Long register(GuestbookDTO dto) {
        log.info("dto.....");
        log.info("Start.....");

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }

    // 수정
    @Override
    public void modify(GuestbookDTO dto) {
        log.info("modify start..");

        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()) {
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    // 삭제
    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {
        // querydsl 처리, 동적 쿼리
        String type = pageRequestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = pageRequestDTO.getKeyword();

        BooleanExpression booleanExpression = qGuestbook.gno.gt(0L); // gno > 0 조건 생성

        booleanBuilder.and(booleanExpression);

        if(type == null || type.trim().length() == 0) {
            // 검색조건이 없는 경우..
            return booleanBuilder;
        }

        // 검색 조건 설정
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
