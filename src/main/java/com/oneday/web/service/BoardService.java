package com.oneday.web.service;

import com.oneday.web.dto.BoardDTO;
import com.oneday.web.entity.Board;
import com.oneday.web.entity.Member;

public interface BoardService {
    Long register(BoardDTO dto);

    default Board dtoToEntity(BoardDTO dto) {
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                        .bno(dto.getBno())
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .writer(member)
                        .build();

        return board;
    }

}
