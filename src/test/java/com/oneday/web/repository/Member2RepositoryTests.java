package com.oneday.web.repository;

import com.oneday.web.entity.Member2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@SpringBootTest
public class Member2RepositoryTests {
    @Autowired
    private Member2Repository member2Repository;
    @Autowired ReviewRepository reviewRepository;

    @Test
    public void insertMembers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member2 member = Member2.builder().email("r" + i + "@ming.com")
                    .pw("1234")
                    .nickname("reviewer" + i).build();

            member2Repository.save(member);
        });
    }

    @Transactional
    @Commit
    @Test
    public void testDeleteMember() {
        Long mid = Long.valueOf(2);

        Member2 member = Member2.builder().mid(mid).build();

        // FK 관계 설정된 자식 테이블 삭제 후 부모 테이블 삭제 필요. deleteByMember 반복 비효율적 => @Query 이용하는게 좋음
        reviewRepository.deleteByMember(member);
        member2Repository.deleteById(mid);

    }
}
