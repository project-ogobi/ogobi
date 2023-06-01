package site.ogobi.ogobi.base.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
import site.ogobi.ogobi.boundedContext.spendingHistory.service.SpendingHistoryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
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

                challengeService.create(member2, "테스트네임1", "테스트내용1", 10000, LocalDate.of(2023, 5, 27), LocalDate.of(2023, 5, 29));
                challengeService.create(member2, "테스트네임2", "테스트내용2", 20000, LocalDate.of(2023, 5, 27), LocalDate.of(2023, 5, 30));

                SpendingHistoryForm historyCoffee = SpendingHistoryForm.builder()
                        .itemName("커피")
                        .itemPrice(4500)
                        .build();

                SpendingHistoryForm historyLunch = SpendingHistoryForm.builder()
                        .itemName("점심")
                        .itemPrice(8000)
                        .build();

                Image image = Image.builder()
                        .originalFileName("test1.png")
                        .uploadFileName("uploadTest1.png")
                        .uploadFilePath("sample")
                        .uploadFileUrl("https://kr.object.ncloudstorage.com/ogobi/sample-folder/4548981b-505e-44d9-93de-586be91ee9ee.png")
                        .build();
                List<Image> sampleImage = new ArrayList<>();
                sampleImage.add(image);

                Image image2 = Image.builder()
                        .originalFileName("test2.png")
                        .uploadFileName("uploadTest2.png")
                        .uploadFilePath("sample")
                        .uploadFileUrl("https://kr.object.ncloudstorage.com/ogobi/sample-folder/7c11096b-1c41-43ef-9bc1-f1ae3843e71a.png")
                        .build();
                List<Image> sampleImage2 = new ArrayList<>();
                sampleImage2.add(image2);

                spendingHistoryService.create(challengeService.findChallengeById(1L).get(), historyCoffee, sampleImage2);
                spendingHistoryService.create(challengeService.findChallengeById(1L).get(), historyLunch, sampleImage);
            }
        };

    }
}
