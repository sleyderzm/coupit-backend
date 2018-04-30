package com.allcode.coupit.handlers;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.net.URL;
import java.util.UUID;

public class S3 {

    public static URL putSignedURL(String contentType){
        URL url;
        try{
            final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
            java.util.Date expiration = new java.util.Date();
            long msec = expiration.getTime();
            msec += 1000 * 60 * 15; // 15 min.
            expiration.setTime(msec);

            GeneratePresignedUrlRequest generatePresignedUrlRequest;

            generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(System.getenv("AWS_PUBLIC_BUCKET"), UUID.randomUUID().toString());
            generatePresignedUrlRequest.addRequestParameter(
                    Headers.S3_CANNED_ACL,
                    CannedAccessControlList.PublicRead.toString()
            );

            generatePresignedUrlRequest.setMethod(HttpMethod.PUT); // Default.
            generatePresignedUrlRequest.setExpiration(expiration);
            generatePresignedUrlRequest.setContentType(contentType);

            url = s3.generatePresignedUrl(generatePresignedUrlRequest);
            return url;
        }catch (Exception e){
        }

        return null;

    }



}
