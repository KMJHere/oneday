package com.oneday.web.service;

import com.oneday.web.dto.GuestbookDTO;
import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.dto.PageResultDTO;
import com.oneday.web.entity.Guestbook;

public interface GuestbookService {
    Long register(GuestbookDTO guestbookDTO);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    GuestbookDTO read(Long gno);

    void remove(Long gno);

    void modify(GuestbookDTO guestbookDTO);


    // insert
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        return entity;
    }

    // select
    default GuestbookDTO entityToDTO(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }
}
