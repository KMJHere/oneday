package com.oneday.web.repository;

import com.oneday.web.entity.Member2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class Member2RepositoryTests {
    @Autowired
    private Member2Repository member2Repository;

    @Test
    public void insertMembers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member2 member = Member2.builder().email("r" + i + "@ming.com")
                    .pw("1234")
                    .nickname("reviewer" + i).build();

            member2Repository.save(member);
        });
    }
}
