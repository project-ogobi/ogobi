package site.ogobi.ogobi.boundedContext.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.image.response.ImageResponse;
import site.ogobi.ogobi.boundedContext.image.service.ImageService;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final PostService postService;


    @Value("${spring.s3.bucket}")
    private String bucketName;

    @PostMapping(value = "/insert-image")
    public ResponseEntity<ImageResponse> insertImage(@RequestParam("file") MultipartFile multipartFile) {
        // https://kr.object.ncloudstorage.com/ogobi/post/UUID.jpg

        List<MultipartFile> multipartFiles = List.of(multipartFile);
        Image image = imageService.uploadFiles(multipartFiles, "post").get(0);

        ImageResponse imageResponse = ImageResponse.builder()
                .originalFileName(image.getOriginalFileName())
                .uploadFileName(image.getUploadFileName())
                .uploadFilePath(image.getUploadFilePath())
                .uploadFileUrl(image.getUploadFileUrl())
                .build();
        // 200
        return ResponseEntity.ok(imageResponse);
    }
}
