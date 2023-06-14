package site.ogobi.ogobi.boundedContext.challenge.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.form.CreateForm;
import site.ogobi.ogobi.boundedContext.challenge.repository.ChallengeRepository;
import site.ogobi.ogobi.boundedContext.image.entity.GraphImage;
import site.ogobi.ogobi.boundedContext.image.repository.GraphImageRepository;
import site.ogobi.ogobi.boundedContext.image.service.ImageService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.entity.MemberTitle;
import site.ogobi.ogobi.boundedContext.member.repository.MemberTitleRepository;
import site.ogobi.ogobi.boundedContext.title.Title;
import site.ogobi.ogobi.boundedContext.title.TitleRepository;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {
    private final Rq rq;
    private final ChallengeRepository challengeRepository;
    private final MemberTitleRepository memberTitleRepository;
    private final TitleRepository titleRepository;
    private final ImageService imageService;
    private final GraphImageRepository graphImageRepository;

    @Transactional
    public void create(Member member, String challengeName, String description, int targetMoney, LocalDate startDate, LocalDate endDate) {

        //TODO: 칭호 가져오는 다른 방법?혹은 개선점? 현재 방식은 문제가 발생하기 쉬워보임.
        if ( member.getChallenge() != null || member.getChallenge().size()==2){
            getTitle();
        }

        Challenge challenge = Challenge
                .builder()
                .member(member)
                .challengeName(challengeName)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .targetMoney(targetMoney)
                .usedMoney(0)
                .achievementRate(0)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();
        challengeRepository.save(challenge);

    }

    public Optional<Challenge> findChallengeById(Long id) {
        return challengeRepository.findById(id);
    }

    @Transactional
    public void update(Member member, @Valid CreateForm updateForm, Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow();

        challenge.updateBasicInfo(updateForm.getChallengeName(),
                updateForm.getDescription(),
                updateForm.getStartDate(),
                updateForm.getEndDate(),
                updateForm.getTargetMoney());

        challengeRepository.save(challenge);
    }

    public boolean canUpdate(Member member, Long challenge_id) {

        for (Challenge challenge : member.getChallenge()) {
            if (Objects.equals(challenge.getId(), challenge_id)) return true;
        }
        return false;
    }

    public String makeFilePathWithChallengeId(Long id) {
        Long memberId = challengeRepository.findById(id).orElseThrow().getMember().getId();
        return "challenge/" + memberId + "/images";
    }

    @Transactional
    public void deleteById(Member member, Long id) {

        Challenge challenge = challengeRepository.findById(id).orElseThrow();

        if(Objects.equals(member.getId(), challenge.getMember().getId())){
            challengeRepository.delete(challenge);
        }
    }

    // 챌린지 완료여부 파악
    @Transactional
    public void checkDone(Long id){
        LocalDate today = LocalDate.now();
        Challenge challenge = challengeRepository.findById(id).orElseThrow();

        int compareStart = today.compareTo(challenge.getStartDate());
        int compareEnd = today.compareTo(challenge.getEndDate());

        // 이미 완료체크된 경우
        if (challenge.isDone() == true) {
            return;
        }

        // 종료시점 비교
        if (compareStart < 0 || compareEnd > 0){
            challenge.setDone(true);
        }

        // 진행중인 챌린지의 목표금액 비교
        if (compareStart >= 0 && compareEnd <= 0 && challenge.getUsedMoney() > challenge.getTargetMoney()) {
            challenge.setDone(true);
            challenge.setSuccess(false);
        }

    }

    public List<Challenge> findByMember(Member member){
        return challengeRepository.findByMember(member);
    }


    // 지출내역 가격 누적그래프 생성
    @Transactional
    public GraphImage generatePriceChart(Long challenge_id) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Challenge challenge = challengeRepository.findById(challenge_id).orElseThrow();

        // 데이터 추가
        LocalDate startDate = challenge.getStartDate();
        LocalDate endDate = challenge.getEndDate();

        Map<LocalDate, Integer> graphData = new HashMap<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            graphData.put(currentDate, 0);
            currentDate = currentDate.plusDays(1);
        }
        for (SpendingHistory sh : challenge.getSpendingHistories()) {
            LocalDate date = sh.getDate();
            graphData.put(date, graphData.get(date) + sh.getPrice());
        }

        Integer currentValue = 0;
        Iterator<Map.Entry<LocalDate, Integer>> iterator = graphData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LocalDate, Integer> entry = iterator.next();
            if (entry.getValue() == 0) {
                iterator.remove();
            }
        }

        Stream<LocalDate> sortedData = graphData.keySet().stream().sorted();
        for (LocalDate localDate : sortedData.toList()) {
            currentValue += graphData.get(localDate);
            dataset.addValue(currentValue, "날짜", localDate);
        }

        // 누적 그래프 생성
        JFreeChart chart = ChartFactory.createLineChart(
                "", // 제목
                "기간", // X-축 레이블
                "누적 지출금액", // Y-축 레이블
                dataset, // 데이터셋
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // 커스텀
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getLegend().setVisible(false);

        // Amazon S3 저장소에 이미지 업로드
        BufferedImage image = chart.createBufferedImage(800, 600);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] chartBytes = baos.toByteArray();

        // 안쓰는 이미지 삭제
        if (graphImageRepository.findByChallengeId(challenge_id).isPresent()) {
            GraphImage found = graphImageRepository.findByChallengeId(challenge_id).get();
            imageService.deleteUploadedFile(found.getUploadFilePath());

            List<GraphImage> temp = new ArrayList<>();
            for (GraphImage graphImage : challenge.getGraphImage()) {
                if (!graphImage.equals(found)) {
                    temp.add(graphImage);
                }
            }
            challenge.setGraphImage(temp);
            graphImageRepository.delete(found);
        }

        GraphImage graphImage = imageService.uploadToS3(chartBytes, challenge_id);
        updateGraphImage(challenge, graphImage);

        return graphImage;
    }

    @Transactional
    public void updateGraphImage(Challenge challenge, GraphImage graphImage) {
        List<GraphImage> temp = new ArrayList<>();
        for (GraphImage image : challenge.getGraphImage()) {
            temp.add(image);
        }
        temp.add(graphImage);

        graphImage.setChallenge(challenge);
        challenge.setGraphImage(temp);
    }

    @Transactional
    public void getTitle(){
        Title aLotChallenge = titleRepository.findById(1L).orElse(null);

        if(aLotChallenge!=null){
            MemberTitle memberTitle = MemberTitle.builder()
                    .member(rq.getMember())
                    .title(aLotChallenge)
                    .build();
            memberTitleRepository.save(memberTitle);
        }

    }

}
