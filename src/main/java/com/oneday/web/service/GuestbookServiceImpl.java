package com.oneday.web.service;

import com.oneday.web.controller.GuestbookController;
import com.oneday.web.dto.GuestbookDTO;
import com.oneday.web.entity.Guestbook;
import com.oneday.web.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository; // 반드시 final로 선언
    @Override
    public Long register(GuestbookDTO dto) {
        log.info("dto.....");
        log.info("Start.....");

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }
}
