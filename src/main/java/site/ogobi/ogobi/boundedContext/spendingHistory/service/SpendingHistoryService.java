package site.ogobi.ogobi.boundedContext.spendingHistory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.image.repository.ImageRepository;
import site.ogobi.ogobi.boundedContext.image.service.ImageService;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;
import site.ogobi.ogobi.boundedContext.spendingHistory.form.SpendingHistoryForm;
import site.ogobi.ogobi.boundedContext.spendingHistory.repository.SpendingHistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpendingHistoryService {
    private final SpendingHistoryRepository spendingHistoryRepository;
    private final ChallengeService challengeService;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Transactional
    public void create(Challenge challenge, SpendingHistoryForm form, List<Image> images){

        challenge.updateUsedMoney(challenge.getUsedMoney() + form.getItemPrice());

        SpendingHistory spendingHistory = SpendingHistory.builder()
                .challenge(challenge)
                .content(form.getItemName())
                .price(form.getItemPrice())
                .description(form.getDescription())
                .build();

        update(spendingHistory, form, images);
        spendingHistoryRepository.save(spendingHistory);
    }

    public Optional<SpendingHistory> findSpendingHistoryById(Long id) {
        return spendingHistoryRepository.findById(id);
    }


    @Transactional
    public void updateSpendingHistory(SpendingHistoryForm form, Long id, List<Image> images) {
        SpendingHistory item = findSpendingHistoryById(id).orElseThrow();
        update(item, form, images);
    }


    @Transactional
    public void createSpendingHistory(SpendingHistoryForm form, Long challenge_id, List<Image> images) {
        Challenge challenge = challengeService.findChallengeById(challenge_id).orElseThrow();
        create(challenge, form, images);
    }

    public SpendingHistoryForm buildSpendingHistoryForm(SpendingHistory spendingHistory) {
        SpendingHistoryForm spendingHistoryForm = SpendingHistoryForm.builder()
                .itemName(spendingHistory.getContent())
                .itemPrice(spendingHistory.getPrice())
                .build();
        return spendingHistoryForm;
    }

    @Transactional
    public void update(SpendingHistory item, SpendingHistoryForm form, List<Image> images) {
        item.setContent(form.getItemName());
        item.setDescription(form.getDescription());

        if (item.getImageFiles() != null && item.getImageFiles().size() > 0) {
            for (Image imageFile : item.getImageFiles()) {
                imageRepository.delete(imageFile);
                imageService.deleteUploadedFile(imageFile.getUploadFilePath());
            }
            item.setImageFiles(new ArrayList<>());
        }
        item.setImageFiles(images);

        for (Image image : images) {
            image.setSpendingHistory(item);
        }
    }

    @Transactional
    public void delete(Long sh_id) {
        SpendingHistory item = spendingHistoryRepository.findById(sh_id).orElseThrow();

        for (Image imageFile : item.getImageFiles()) {
            imageService.deleteUploadedFile(imageFile.getUploadFilePath());
        }

        spendingHistoryRepository.delete(item);
    }
}
