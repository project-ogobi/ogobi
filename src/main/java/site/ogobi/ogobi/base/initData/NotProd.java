package site.ogobi.ogobi.base.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.spendingHistory.service.SpendingHistoryService;

import java.time.LocalDate;

@Configuration
@Profile({"dev", "test"})
public class NotProd {

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChallengeService challengeService,
            SpendingHistoryService spendingHistoryService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member member1 = memberService.join("user1", "1234");
                Member member2 = memberService.join("test1", "test1");

                challengeService.create(member2, "테스트네임1", "테스트내용1", 10000, LocalDate.of(2023,5,27), LocalDate.of(2023,5,29) );
                challengeService.create(member2, "테스트네임2", "테스트내용2", 20000,  LocalDate.of(2023,5,27), LocalDate.of(2023,5,30));

                spendingHistoryService.create(challengeService.findChallengeById(1L).get(), "커피", 4500, "설명");
                spendingHistoryService.create(challengeService.findChallengeById(1L).get(), "점심", 8000, "설명");
            }
        };

    }
}
