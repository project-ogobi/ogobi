package site.ogobi.ogobi.boundedContext.spendingHistory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;
import site.ogobi.ogobi.boundedContext.spendingHistory.repository.SpendingHistoryRepository;

@Service
@RequiredArgsConstructor
public class SpendingHistoryService {
    private final SpendingHistoryRepository spendingHistoryRepository;

    @Transactional
    public void create(Challenge challenge, String content, int price){

        challenge.updateUsedMoney(challenge.getUsedMoney() + price);

        SpendingHistory spendingHistory = SpendingHistory.builder()
                .content(content)
                .price(price)
                .challenge(challenge)
                .build();

        spendingHistoryRepository.save(spendingHistory);
    }
}
