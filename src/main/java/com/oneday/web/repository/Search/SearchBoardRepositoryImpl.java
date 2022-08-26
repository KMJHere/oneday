package com.oneday.web.repository.Search;

import com.oneday.web.entity.Board;
import com.oneday.web.entity.QBoard;
import com.oneday.web.entity.QMember;
import com.oneday.web.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

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
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        // member 테이블 추가
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        // board와 reply left join
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // 기본 select
        // jpqlQuery.select(board).where(board.bno.eq(1L));
        jpqlQuery.select(board, member.email, reply.count())
                        .groupBy(board);

        log.info("------------------");
        log.info(jpqlQuery);
        log.info("------------------");

        List<Board> result = jpqlQuery.fetch();

        log.info(result);

        return null;
    }

    // Tuple 객체 사용
    @Override
    public Board search2() {
        log.info("Search2 Start..");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());

        tuple.groupBy(board);

        log.info("------------------");
        log.info(tuple);
        log.info("------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage running..");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        // BooleanBuilder > 조건절 구성..Start
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null) {
            String[] typeArr = type.split("");
            // 검색 조건
            BooleanBuilder condBuilder = new BooleanBuilder();

            for(String str : typeArr) {
                switch (str) {
                    case "t":
                        condBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        condBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        condBuilder.or(member.email.contains(keyword));
                        break;
                }
            }

            booleanBuilder.and(condBuilder);
        }

        tuple.where(booleanBuilder);

        // order By 처리
        Sort sort  = pageable.getSort();

        // tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            String sProp = order.getProperty();

            // sort 객체 속성 등..PathBuilder 이용해서 처리
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(sProp)));
        });

        tuple.groupBy(board);

        // JPQL page 처리,, offset, size 이용
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("FetchCount: " + count);

        return new PageImpl<Object[]>(
                result.stream().map(amDat -> amDat.toArray()).collect(Collectors.toList()), pageable, count);

    }
}
