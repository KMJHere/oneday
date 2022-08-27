package com.oneday.web.service;

import com.oneday.web.dto.BoardDTO;
import com.oneday.web.dto.PageRequestDTO;
import com.oneday.web.dto.PageResultDTO;
import com.oneday.web.entity.Board;
import com.oneday.web.entity.Member;
import com.oneday.web.repository.BoardRepository;
import com.oneday.web.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository; // 자동 주입 final

    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {
        log.info("뭐가 있나..?" + dto);

        Board board = dtoToEntity(dto);

        boardRepository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info("getList...");

        // Function<T, R>는 T타입의 인자를 받고, R타입의 객체를 리턴
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2]));

        // Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getPageable(Sort.by("bno").descending())
            );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = boardRepository.getBoardByBno(bno);

        Object[] arr = (Object[])result;

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    // 삭제 기능 구현, 트랙잭션 추가
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        // 댓글 삭제
        replyRepository.deleteByBno(bno);

        boardRepository.deleteById(bno);
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        // getone 대신 findById
        Board board = boardRepository.findById(boardDTO.getBno()).orElseThrow(() -> new IllegalArgumentException("no such data"));

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        boardRepository.save(board);
    }
}
