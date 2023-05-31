package site.ogobi.ogobi.base.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
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
                Member member1 = memberService.join("user1", "1234", "첫번째멤버");
                Member member2 = memberService.join("test1", "test1", "원투");

                challengeService.create(member2, "테스트네임1", "테스트내용1", 10000, LocalDate.of(2023,5,27), LocalDate.of(2023,5,29) );
                challengeService.create(member2, "테스트네임2", "테스트내용2", 20000,  LocalDate.of(2023,5,27), LocalDate.of(2023,5,30));

                SpendingHistoryForm historyCoffee = SpendingHistoryForm.builder()
                        .itemName("커피")
                        .itemPrice(4500)
                        .build();

                SpendingHistoryForm historyLunch = SpendingHistoryForm.builder()
                        .itemName("점심")
                        .itemPrice(8000)
                        .build();

                spendingHistoryService.create(challengeService.findChallengeById(1L).get(), historyCoffee, null);
                spendingHistoryService.create(challengeService.findChallengeById(1L).get(), historyLunch, null);
            }
        };

    }
}
