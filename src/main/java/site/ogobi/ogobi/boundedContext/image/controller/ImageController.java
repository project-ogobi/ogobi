package site.ogobi.ogobi.boundedContext.image.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.image.entity.Image;
import site.ogobi.ogobi.boundedContext.image.service.ImageService;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final AmazonS3Client amazonS3Client;
    private final ImageService imageService;
    private final PostService postService;


    @Value("${spring.s3.bucket}")
    private String bucketName;

    @PostMapping(value = "/insert-image")
    public ResponseEntity<String> insertImage(@RequestParam("file") MultipartFile multipartFiles) {
        String filePath = postService.makeFilePathWithPostId(2L);
        String url = imageService.uploadFilesSample(List.of(multipartFiles)).get(0).getUploadFileUrl();

        return ResponseEntity.ok(url);
    }

    @PostMapping("/insertPost")
    public void insertPost(@RequestBody Map<String, Object> requestBody) {
        System.out.println(requestBody);
    }
}