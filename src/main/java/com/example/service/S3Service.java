//package com.example.service;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//public class S3Service {
//
//    private final AmazonS3 s3Client;
//
//    @Value("${yandex.cloud.s3.bucket-name}")
//    private String bucketName;
//
//    public S3Service(AmazonS3 s3Client) {
//        this.s3Client = s3Client;
//    }
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(file.getSize());
//        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
//                .withCannedAcl(CannedAccessControlList.PublicRead));
//        return fileName;
//    }
//
//    public byte[] downloadFile(String fileName) throws IOException {
//        return s3Client.getObject(bucketName, fileName).getObjectContent().readAllBytes();
//    }
//
//    public void deleteFile(String fileName) {
//        s3Client.deleteObject(bucketName, fileName);
//    }
//}