package site.ogobi.ogobi.base.objectStorage;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BucketConfig {
    private final String endPoint = "https://kr.object.ncloudstorage.com";
    private final String regionName = "kr-standard";
    private final String accessKey = "accessKey";
    private final String secretKey = "secretKey";

    // S3 client
    private final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();

    private final String bucketName = "ogobi";

    public void uploadFile(){

        // create folder
        String folderName = "sample-folder/";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);

        try {
            s3.putObject(putObjectRequest);
            System.out.format("Folder %s has been created.\n", folderName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        // upload local file
        String objectName = "sample.txt";
        String filePath = "C:/temp/sample.txt";

        File file = null;
        try {
            file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            writer.write("This is a sample file content.");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            s3.putObject(bucketName, objectName, file);
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void showFileList(){
        // list all in the bucket
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withMaxKeys(300);

            ObjectListing objectListing = s3.listObjects(listObjectsRequest);

            System.out.println("Object List:");
            while (true) {
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    System.out.println("    name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
                }

                if (objectListing.isTruncated()) {
                    objectListing = s3.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
        } catch (AmazonS3Exception e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        // top level folders and files in the bucket
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withDelimiter("/")
                    .withMaxKeys(300);

            ObjectListing objectListing = s3.listObjects(listObjectsRequest);

            System.out.println("Folder List:");
            for (String commonPrefixes : objectListing.getCommonPrefixes()) {
                System.out.println("    name=" + commonPrefixes);
            }

            System.out.println("File List:");
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println("    name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
            }
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void setCORS(){
        // Create CORS rules.
        List<CORSRule.AllowedMethods> methodRule = new ArrayList<>();
        methodRule.add(CORSRule.AllowedMethods.PUT);
        methodRule.add(CORSRule.AllowedMethods.GET);
        methodRule.add(CORSRule.AllowedMethods.POST);
        CORSRule rule = new CORSRule().withId("CORSRule")
                .withAllowedMethods(methodRule)
                .withAllowedHeaders(Arrays.asList(new String[] { "*" }))
                .withAllowedOrigins(Arrays.asList(new String[] { "*" }))
                .withMaxAgeSeconds(3000);

        List<CORSRule> rules = new ArrayList<CORSRule>();
        rules.add(rule);

        // Add rules to new CORS configuration.
        BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration();
        configuration.setRules(rules);

        // Set the rules to CORS configuration.
        s3.setBucketCrossOriginConfiguration(bucketName, configuration);

        // Get the rules for CORS configuration.
        configuration = s3.getBucketCrossOriginConfiguration(bucketName);

        if (configuration == null) {
            System.out.println("Configuration is null.");
        } else {
            System.out.println("Configuration has " + configuration.getRules().size() + " rules\n");

            for (CORSRule getRule : configuration.getRules()) {
                System.out.println("Rule ID: " + getRule.getId());
                System.out.println("MaxAgeSeconds: " + getRule.getMaxAgeSeconds());
                System.out.println("AllowedMethod: " + getRule.getAllowedMethods());
                System.out.println("AllowedOrigins: " + getRule.getAllowedOrigins());
                System.out.println("AllowedHeaders: " + getRule.getAllowedHeaders());
                System.out.println("ExposeHeader: " + getRule.getExposedHeaders());
                System.out.println();
            }
        }
    }
}
