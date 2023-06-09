package site.ogobi.ogobi.base.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.ogobi.ogobi.boundedContext.auth.entity.SignUp;
import site.ogobi.ogobi.boundedContext.auth.service.AuthService;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.repository.MemberRepository;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.post.service.PostService;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
import site.ogobi.ogobi.boundedContext.spendingHistory.service.SpendingHistoryService;
import site.ogobi.ogobi.boundedContext.title.Title;
import site.ogobi.ogobi.boundedContext.title.TitleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@Profile({"dev", "test"})
public class NotProd {

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            TitleRepository titleRepository,
            ChallengeService challengeService,
            SpendingHistoryService spendingHistoryService,
            AuthService authService
    ) {

        return args -> {
            SignUp signup1 = SignUp.builder()
                    .nickname("첫째멤")
                    .loginId("user1")
                    .email("user1@ogobi.com")
                    .password("1234")
                    .build();

            SignUp signup2 = SignUp.builder()
                    .nickname("멤투")
                    .loginId("test1")
//                    .email("test1@ogobi.com")
                    .email("lucas0209@naver.com")
                    .password("test1")
                    .build();
//칭호
            Title aLotChallenge = Title.builder()
                    .name("명예 도전자").build();
            titleRepository.save(aLotChallenge);

            authService.signUp(signup1);
            authService.signUp(signup2);

            Member member = memberService.getMember("test1");

            challengeService.create(member, "테스트네임1", "테스트내용1", 10000, LocalDate.of(2023, 5, 27), LocalDate.of(2023, 5, 29));
            challengeService.create(member, "테스트네임2", "테스트내용2", 20000, LocalDate.of(2023, 5, 27), LocalDate.of(2023, 5, 30));
            challengeService.create(member, "테스트네임3", "테스트내용3", 20000, LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 28));
            challengeService.create(member, "테스트네임4", "테스트내용4", 20000, LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 28));
            challengeService.create(member, "테스트네임5", "테스트내용5", 20000, LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 28));
            challengeService.create(member, "테스트네임6", "테스트내용6", 20000, LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 28));
            challengeService.create(member, "테스트네임7", "테스트내용7", 20000, LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 28));
            challengeService.create(member, "테스트네임8", "테스트내용8", 20000, LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 28));
            challengeService.create(member, "테스트네임9", "테스트내용9", 20000, LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 28));
            challengeService.create(member, "테스트네임10", "테스트내용10", 30000, LocalDate.of(2023, 6, 1), LocalDate.of(2023, 7, 1));


            LocalDate localDate = LocalDate.parse("2023-06-11");
            LocalDate localDate_v2 = LocalDate.parse("2023-06-12");
            SpendingHistoryForm historyCoffee = SpendingHistoryForm.builder()
                    .itemName("커피")
                    .itemPrice(4500)
                    .date(localDate)
                    .build();

            SpendingHistoryForm historyLunch = SpendingHistoryForm.builder()
                    .itemName("점심")
                    .itemPrice(8000)
                    .date(localDate_v2)
                    .build();

            Image image = Image.builder()
                    .originalFileName("test1.png")
                    .uploadFileName("uploadTest1.png")
                    .uploadFilePath("sample")
                    .uploadFileUrl("https://kr.object.ncloudstorage.com/ogobi/challenge/2/images/f57bd32c-9722-489d-bdce-b10be9f3616f.jpg")
                    .build();
            List<Image> sampleImage = new ArrayList<>();
            sampleImage.add(image);

            Image image2 = Image.builder()
                    .originalFileName("test2.png")
                    .uploadFileName("uploadTest2.png")
                    .uploadFilePath("sample")
                    .uploadFileUrl("https://kr.object.ncloudstorage.com/ogobi/challenge/2/images/23271811-e1b8-4200-a09c-3e682536aa76.jpg")
                    .build();
            List<Image> sampleImage2 = new ArrayList<>();
            sampleImage2.add(image2);

            spendingHistoryService.create(challengeService.findChallengeById(1L).get(), historyLunch, sampleImage);
            spendingHistoryService.create(challengeService.findChallengeById(1L).get(), historyCoffee, sampleImage2);


        };
    }

}
