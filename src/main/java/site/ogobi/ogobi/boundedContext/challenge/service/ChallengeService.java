package site.ogobi.ogobi.boundedContext.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.repository.ChallengeRepository;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

}
