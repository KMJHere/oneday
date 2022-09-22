package com.oneday.web.security;

import com.oneday.web.entity.ClubMember;
import com.oneday.web.entity.ClubMemberRole;
import com.oneday.web.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {
    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 한명의 회원이 여러 권한을 가질 수 있도록..
    @Test
    public void insertDevMember() {
        /*
            1 - 80까지는 USER ROLE
            81 - 90까지는 USER, MANAGER ROLE
            91 - 100까지는 USER, MANAGER, ADMIN ROLE
        */
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@kmj.org")
                    .name("사용자" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            // default USER ROLE
            clubMember.addMemberRole(ClubMemberRole.USER);

            if(i > 80) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if(i > 90) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }

            clubMemberRepository.save(clubMember);
        });
    }

    @Test
    public void testRead() {
        Optional<ClubMember> result = clubMemberRepository.findByEmail("user91@kmj.org", false);

        ClubMember clubMember = result.get();

        System.out.print("clubMember: " + clubMember);
    }
}
