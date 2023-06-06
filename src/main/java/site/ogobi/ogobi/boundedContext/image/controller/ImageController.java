package site.ogobi.ogobi.boundedContext.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.image.service.ImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @DeleteMapping(value ="/delete-image/{id}")
    public ResponseEntity<Long> deleteImage(@PathVariable Long id){

        Long deleteNum = imageService.deleteUploadedFileById(id);
        return ResponseEntity.ok(deleteNum);
    }

    @PostMapping("/update-image/{id}")
    public ResponseEntity<String> reloadImage(@RequestParam("file") MultipartFile multipartFiles, @PathVariable Long id){
        //수정
        String fileUrl = imageService.updateSpendingHistoryImage(multipartFiles, id);
        return ResponseEntity.ok(fileUrl);
    }


}
