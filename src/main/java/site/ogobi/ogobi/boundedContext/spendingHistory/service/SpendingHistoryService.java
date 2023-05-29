package site.ogobi.ogobi.boundedContext.spendingHistory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
import site.ogobi.ogobi.boundedContext.spendingHistory.repository.SpendingHistoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpendingHistoryService {
    private final SpendingHistoryRepository spendingHistoryRepository;
    private final ChallengeService challengeService;

    @Transactional
    public void create(Challenge challenge, String content, int price, String description){

        challenge.updateUsedMoney(challenge.getUsedMoney() + price);

        SpendingHistory spendingHistory = SpendingHistory.builder()
                .content(content)
                .price(price)
                .challenge(challenge)
                .description(description)
                .build();

        spendingHistoryRepository.save(spendingHistory);
    }

    public Optional<SpendingHistory> findSpendingHistoryById(Long id) {
        return spendingHistoryRepository.findById(id);
    }

    @Transactional
    public void createSpendingHistory(SpendingHistoryForm form, Long id, Long challenge_id) {
        if (findSpendingHistoryById(id).isPresent()) {
            SpendingHistory item = findSpendingHistoryById(id).get();
            item.updateDescription(form.getDescription());
        } else {
            Challenge challenge = challengeService.findChallengeById(challenge_id).orElseThrow();
            create(challenge, form.getItemName(), form.getItemPrice(), form.getDescription());
        }

    }
}
