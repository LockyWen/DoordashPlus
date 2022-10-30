package com.mincong.doordashplus.config;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
public class AWSConfig {

    @Value("${s3.accesskey}")
    private String ACCESS_KEY;

    @Value("${s3.privatekey}")
    private String PRIVATE_KEY;


    public AWSCredentials credentials(){
        return new BasicAWSCredentials(ACCESS_KEY, PRIVATE_KEY);
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public void createS3Bucket(String bucketName) {
        AmazonS3 amazonS3Client = amazonS3();
        if(amazonS3Client.doesBucketExist(bucketName)) {
            log.info("BUCKET CREATED");
            return;
        }
        amazonS3Client.createBucket(bucketName);
    }

    public List<Bucket> listBuckets(){
        return amazonS3().listBuckets();
    }

    public void deleteBucket(String bucketName){
        AmazonS3 amazonS3Client = amazonS3();
        try {
            amazonS3Client.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
            return;
        }
    }

//    public void putObject(String bucketName, BucketObjectRepresentaion representation, boolean publicObject) throws IOException {
//
//        String objectName = representation.getObjectName();
//        String objectValue = representation.getText();
//
//        File file = new File("." + File.separator + objectName);
//        FileWriter fileWriter = new FileWriter(file, false);
//        PrintWriter printWriter = new PrintWriter(fileWriter);
//        printWriter.println(objectValue);
//        printWriter.flush();
//        printWriter.close();
//
//        try {
//            var putObjectRequest = new PutObjectRequest(bucketName, objectName, file).withCannedAcl(CannedAccessControlList.PublicRead);
//            amazonS3Client.putObject(putObjectRequest);
//        } catch (Exception e){
//            log.error("Some error has ocurred.");
//        }
//
//    }
}
