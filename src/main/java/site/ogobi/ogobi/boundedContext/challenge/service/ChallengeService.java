package site.ogobi.ogobi.boundedContext.challenge.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.form.CreateForm;
import site.ogobi.ogobi.boundedContext.challenge.repository.ChallengeRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Transactional
    public void create(Member member, String challengeName, String description, int targetMoney, LocalDate startDate, LocalDate endDate) {
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

    public String makeFilePathWithChallengeId(Long id){
        Long memberId = challengeRepository.findById(id).orElseThrow().getMember().getId();
        return "challenge/" + memberId + "/images";
    }
}
