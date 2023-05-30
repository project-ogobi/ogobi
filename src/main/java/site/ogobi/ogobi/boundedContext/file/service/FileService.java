package site.ogobi.ogobi.boundedContext.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.file.entity.FileDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }
    public List<FileDto> uploadFiles(List<MultipartFile> multipartFiles){

        return uploadFiles(multipartFiles, "sample-folder");
    }

    //NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) spending-history/images
    public List<FileDto> uploadFiles(List<MultipartFile> multipartFiles, String filePath) {

        List<FileDto> s3files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {

            String originalFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getUuidFileName(originalFileName);
            String uploadFileUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {

                String keyName = filePath + "/" + uploadFileName;

                // S3에 폴더 및 파일 업로드
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                // S3에 업로드한 폴더 및 파일 URL
                uploadFileUrl = "https://kr.object.ncloudstorage.com/ogobi/" + keyName;

            } catch (IOException e) {
                e.printStackTrace();
            }

            s3files.add(
                    FileDto.builder()
                            .originalFileName(originalFileName)
                            .uploadFileName(uploadFileName)
                            .uploadFilePath(filePath)
                            .uploadFileUrl(uploadFileUrl)
                            .build());
        }

        return s3files;
    }
}
