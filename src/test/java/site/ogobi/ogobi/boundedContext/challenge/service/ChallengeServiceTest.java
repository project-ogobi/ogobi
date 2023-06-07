package site.ogobi.ogobi.boundedContext.challenge.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.repository.ChallengeRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.repository.MemberRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class ChallengeServiceTest {

    private Rq rq;
    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("create 테스트")
    public void t001() throws Exception{
        //given
        String challengeName = "테스트 챌린지 제목1";
        String description = "테스트 챌린지 내용1";
        int targetMoney = 100000;

        Member member = Member.builder().username("testuser1").password("1234").build();
        memberRepository.save(member);

        //when
        challengeService.create(member, challengeName, description, targetMoney, LocalDate.now(), LocalDate.now());

        //then
        Challenge savedChallenge = challengeRepository.findById(3L).get();

        // 챌린지가 null이 아닌지 확인
        Assertions.assertNotNull(savedChallenge);

        // 챌린지 필드 값 확인
        Assertions.assertEquals(challengeName, savedChallenge.getChallengeName());
        Assertions.assertEquals(description, savedChallenge.getDescription());
        Assertions.assertEquals(targetMoney, savedChallenge.getTargetMoney());
    }

    @Test
    @DisplayName("delete 테스트")
    public void t002() throws Exception{
        //given
        String challengeName = "테스트 챌린지 제목2";
        String description = "테스트 챌린지 내용2";
        int targetMoney = 100000;

        Member member = Member.builder().username("testuser2").password("1234").build();
        memberRepository.save(member);

        //when
        challengeService.create(member, challengeName, description, targetMoney, LocalDate.now(), LocalDate.now());

        long countNum = challengeRepository.count();

        //then
        challengeService.deleteById(member,4L);
        Assertions.assertEquals(challengeRepository.count(), countNum-1);

    }
}