package com.oneday.web.repository;

import com.oneday.web.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {
    @Autowired MemberRepository memberRepository;

    @Test
    public void insertMember() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@kmj.com")
                    .password("1111")
                    .name("user" + i)
                    .build();

            memberRepository.save(member);
        });
    }
}
