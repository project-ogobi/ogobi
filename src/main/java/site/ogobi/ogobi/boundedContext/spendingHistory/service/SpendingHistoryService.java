package site.ogobi.ogobi.boundedContext.spendingHistory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
import site.ogobi.ogobi.boundedContext.spendingHistory.repository.SpendingHistoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpendingHistoryService {
    private final SpendingHistoryRepository spendingHistoryRepository;
    private final ChallengeService challengeService;

    @Transactional
    public void create(Challenge challenge, SpendingHistoryForm form, List<Image> images){

        challenge.updateUsedMoney(challenge.getUsedMoney() + form.getItemPrice());

        SpendingHistory spendingHistory = SpendingHistory.builder()
                .challenge(challenge)
                .content(form.getItemName())
                .price(form.getItemPrice())
                .description(form.getDescription())
                .imageFiles(images)
                .build();

        spendingHistoryRepository.save(spendingHistory);
    }

    public Optional<SpendingHistory> findSpendingHistoryById(Long id) {
        return spendingHistoryRepository.findById(id);
    }


    @Transactional
    public void updateSpendingHistory(SpendingHistoryForm form, Long id, List<Image> images) {
        SpendingHistory item = findSpendingHistoryById(id).orElseThrow();
        item.update(form.getItemName(), form.getDescription(), images);
    }


    @Transactional
    public void createSpendingHistory(SpendingHistoryForm form, Long challenge_id, List<Image> images) {
        Challenge challenge = challengeService.findChallengeById(challenge_id).orElseThrow();
        create(challenge, form, images);
    }
}
