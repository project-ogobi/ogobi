package site.ogobi.ogobi.boundedContext.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.ogobi.ogobi.boundedContext.image.entity.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    @Value("${image.upload-dir}")
    private String uploadDir;

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }
    public List<Image> uploadFilesSample(List<MultipartFile> multipartFiles){

        return uploadFiles(multipartFiles, "sample-folder");
    }

    //NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) spending-history/images
    public List<Image> uploadFiles(List<MultipartFile> multipartFiles, String filePath) {

        if (multipartFiles.get(0).getOriginalFilename().length() == 0) {
            return new ArrayList<>();
        }

        List<Image> s3files = new ArrayList<>();

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
                                .withCannedAcl(CannedAccessControlList.PublicRead)); // TODO : POST 이미지 업로드 CDN 설정?


                // S3에 업로드한 폴더 및 파일 URL
                uploadFileUrl = "https://kr.object.ncloudstorage.com/ogobi/" + keyName;

                s3files.add(
                        Image.builder()
                                .originalFileName(originalFileName)
                                .uploadFileName(uploadFileName)
                                .uploadFilePath(keyName)
                                .uploadFileUrl(uploadFileUrl)
                                .build());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return s3files;
    }

    public String deleteUploadedFile(String filePath) {

        try {
            if (amazonS3Client.doesObjectExist(bucketName, filePath)) {
                amazonS3Client.deleteObject(bucketName, filePath);
                return "success";
            } else {
                return "file not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error occurred";
        }
    }
}
