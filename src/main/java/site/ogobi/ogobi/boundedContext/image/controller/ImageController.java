package site.ogobi.ogobi.boundedContext.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.image.service.ImageService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/upload")
    public String getUpload(){
        return "upload";
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFilesSample(
        @RequestPart(value = "files") List<MultipartFile> multipartFiles) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(imageService.uploadFiles(multipartFiles));
    }
}
