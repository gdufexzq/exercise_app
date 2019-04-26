package com.gdufe.exercise_app.util;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Minio服务的工具类
 */
public class MinioUtil {

    private static Logger logger = LoggerFactory.getLogger(MinioUtil.class);

    @Autowired
    private MinioClient minioClient;

    /**
     * 创建一个并初始化一个桶
     */
    private static void createBucket(MinioClient minioClient, String bucket) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException {
        /**
         * 判断桶是否存在
         * //        // 设置桶策略
         * //        minioClient.setBucketPolicy(bucket, "files", PolicyType.READ_WRITE);
         */
        boolean isExist = minioClient.bucketExists(bucket);
        if(!isExist) {
            //创建存储桶
            minioClient.makeBucket(bucket);
        }

    }


    /**
     *
     * @param minioClient 客户端
     * @param bucket  桶
     * @param fileUrl  文件的url
     * @return
     */
    public static String FileUploadByUrl(MinioClient minioClient, String bucket,
                                         String fileUrl) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, InvalidArgumentException {

        createBucket(minioClient, bucket);
        String fileType = getFileType(fileUrl);
        //使用UUID生成新的文件名
//        String newFileUrl =  UUID.fromString(fileUrl) + fileType;
        String newFileUrl =  UUID.randomUUID() + fileType;
        logger.info("newFileUrl:" + newFileUrl);
        //使用minio存储文件
        minioClient.putObject(bucket, newFileUrl, fileUrl);
        //得到文件新的url,并返回
        String url = minioClient.getObjectUrl(bucket, newFileUrl);
        logger.info("url:" + url);
        return url;
    }

    public static String FileUploadByStream(MinioClient minioClient, String bucket,
                                            String fileUrl, InputStream inputStream) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, InvalidArgumentException {

        createBucket(minioClient, bucket);
        String fileType = getFileType(fileUrl);
        //使用UUID生成新的文件名
        String newFileUrl =  UUID.randomUUID() + fileType;
        logger.info("newFileUrl:" + newFileUrl);

        //fileType包含了一个.需要去掉
        String contentType = fileType.replaceAll(".", "");
        //使用minio存储文件
        minioClient.putObject(bucket, newFileUrl, inputStream, contentType);
        //得到文件新的url,并返回
        String url = minioClient.getObjectUrl(bucket, newFileUrl);
        logger.info("url:" + url);
        return url;
    }

    /**
     * 通过.获取文件的类型
     * @param fileUrl
     * @return
     */
    public static String getFileType(String fileUrl) {
        if(fileUrl == null) {
            return null;
        }else {
            if(fileUrl.contains(".")){
                String fileType = fileUrl.substring(fileUrl.lastIndexOf(fileUrl, 1));
                return fileType;
            }else {
                return null;
            }
        }

    }




}
