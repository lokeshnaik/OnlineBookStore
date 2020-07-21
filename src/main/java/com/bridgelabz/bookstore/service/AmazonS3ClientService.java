package com.bridgelabz.bookstore.service;

import org.springframework.web.multipart.MultipartFile;


public interface AmazonS3ClientService
{
    void uploadFileToS3Bucket(MultipartFile multipartFile);

    void deleteFileFromS3Bucket(String fileName);
}
