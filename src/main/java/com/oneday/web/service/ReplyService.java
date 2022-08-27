package com.oneday.web.service;

import com.oneday.web.dto.ReplyDTO;
import com.oneday.web.entity.Board;
import com.oneday.web.entity.Reply;

import java.util.List;

public interface ReplyService {
    // 댓글 등록
    Long register(ReplyDTO replyDTO);

    // 특정 게시물의 댓글 목록
    List<ReplyDTO> getList(Long bno);

    // 댓글 수정
    void modify(ReplyDTO replyDTO);

    // 댓글 삭제
    void remove(Long bno);

    // ReplyDTO -> Reply 객체로 변환 Board 객체 처리..
    default Reply dtoToEntity(ReplyDTO replyDto) {
        Board board = Board.builder().bno(replyDto.getBno()).build();

        Reply reply = Reply.builder().rno(replyDto.getRno()).text(replyDto.getText()).replyer(replyDto.getReplyer()).board(board).build();

        return reply;
    }

    // Reply 객체 -> ReplyDTO 변환, Board 객체 필요 x 게시글 번호만..
    default ReplyDTO entityToDTO(Reply reply) {
        ReplyDTO dto = ReplyDTO.builder().rno(reply.getRno()).text(reply.getText()).replyer(reply.getReplyer()).regDate(reply.getRegDate()).modDate(reply.getModDate()).build();

        return dto;
    }
}
