package site.ogobi.ogobi.boundedContext.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.repository.ChallengeRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Transactional
    public void create(Member member, String challengeName, String description, int targetMoney) {
        Challenge challenge = Challenge
                .builder()
                .member(member)
                .challengeName(challengeName)
                .description(description)
                //startDate,endDate 추가하기
                .targetMoney(targetMoney)
                .usedMoney(0)
                .achievementRate(0)
                .build();

        challengeRepository.save(challenge);
    }

    public Optional<Challenge> findChallengeById(Long id) {
        return challengeRepository.findById(id);
    }
}
