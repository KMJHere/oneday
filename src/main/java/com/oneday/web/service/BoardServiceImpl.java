package com.oneday.web.service;

import com.oneday.web.dto.BoardDTO;
import com.oneday.web.entity.Board;
import com.oneday.web.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository; // 자동 주입 final

    public Long register(BoardDTO dto) {
        log.info("뭐가 있나..?" + dto);

        Board board = dtoToEntity(dto);

        boardRepository.save(board);

        return board.getBno();
    }
}
