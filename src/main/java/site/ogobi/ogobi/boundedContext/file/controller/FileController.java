package site.ogobi.ogobi.boundedContext.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.file.service.FileService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/upload")
    public String getUpload(){
        return "upload";
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFiles(
        @RequestParam(value = "filePath") String filePath,
        @RequestPart(value = "files") List<MultipartFile> multipartFiles) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(fileService.uploadFiles(filePath, multipartFiles));
    }
}
