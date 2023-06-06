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
    public ResponseEntity<String> deleteImage(@PathVariable Long id){

        imageService.deleteUploadedFileById(id);
        String deleteNum = "deleted";
        return ResponseEntity.ok(deleteNum);
    }

    @PostMapping("/reload-image/{id}")
    public ResponseEntity<String> reloadImage(@PathVariable Long id){
        //수정
        imageService.updateImage();
        String deleteNum = "updated";
        return ResponseEntity.ok(deleteNum);
    }


}
