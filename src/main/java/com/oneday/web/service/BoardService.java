package com.oneday.web.service;

import com.oneday.web.dto.BoardDTO;
import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.dto.PageResultDTO;
import com.oneday.web.entity.Board;
import com.oneday.web.entity.Member;

public interface BoardService {
    Long register(BoardDTO dto);

    void modify(BoardDTO boardDTO);

    // 목록처리
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    // 게시물번호를 파라미터로 받는 단건 조회
    BoardDTO get(Long bno);

    // 삭제
    void removeWithReplies(Long bno);

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

    // BoardService 인터페이스에 추가하는 entityToDTO()
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCnt(replyCount.intValue()) // 리턴값 -> long, int로 처리
                .build();

        return boardDTO;
    }
}
