package com.oneday.web.repository.Search;

import com.oneday.web.entity.Board;
import com.oneday.web.entity.QBoard;
import com.oneday.web.entity.QReply;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    public SearchBoardRepositoryImpl() {
        // 부모 클래스 생성자 호출, 도메인 클래스 지정(null x) > QuerydslRepositorySupport에 생성자 존재
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("Search1 start..");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> jpqlQuery = from(board);
        // board와 reply left join
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        jpqlQuery.select(board).where(board.bno.eq(1L));

        log.info("------------------");
        log.info(jpqlQuery);
        log.info("------------------");

        List<Board> result = jpqlQuery.fetch();

        return null;
    }
}
