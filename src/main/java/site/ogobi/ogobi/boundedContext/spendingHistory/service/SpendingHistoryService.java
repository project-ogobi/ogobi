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
import java.util.stream.Collectors;

@Slf4j
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

        mappingImagesAndHistoryWithForm(spendingHistory, form, images);
        spendingHistoryRepository.save(spendingHistory);
    }
    private void mappingImagesAndHistoryWithForm(SpendingHistory item, SpendingHistoryForm form, List<Image> images){
        item.setContent(form.getItemName());
        item.setDescription(form.getDescription());

        item.setImageFiles(images);
        for (Image image : images) {
            image.setSpendingHistory(item);
        }
    }

    public Optional<SpendingHistory> findSpendingHistoryById(Long id) {
        return spendingHistoryRepository.findById(id);
    }


    @Transactional
    public void updateSpendingHistory(SpendingHistoryForm form, Long id, List<Image> images, List<String> listId) {
        SpendingHistory item = findSpendingHistoryById(id).orElseThrow();
        update(item, form, images, listId);
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
                .description(spendingHistory.getDescription())
                .build();
        return spendingHistoryForm;
    }

    @Transactional
    public void update(SpendingHistory item, SpendingHistoryForm form, List<Image> images, List<String> listId) {

        List<Image> newImages = new ArrayList<>();

        if (item.getImageFiles() != null && item.getImageFiles().size() > 0) {
            for (Image image : item.getImageFiles()) {
                if (!listId.contains(String.valueOf(image.getId()))) {
                    imageRepository.delete(image);
                    imageService.deleteUploadedFile(image.getUploadFilePath());
                } else {
                    newImages.add(image);
                }

            }
        }

        newImages.addAll(images);
        mappingImagesAndHistoryWithForm(item, form, newImages);
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
