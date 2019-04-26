package com.gdufe.exercise_app.config;

import com.gdufe.exercise_app.exception.SystemErrorException;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minioClient配置
 *
 */
@Configuration
public class MinioClientConfig {

    /**
     * 存储的服务的url
     */
    @Value("${minio.urlPath}")
    private String urlPath;

    /**
     * 相当于mysql的用户名，
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * 相当于mysql的用户密码，
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        try {
            return new MinioClient(urlPath, accessKey, secretKey);
        } catch (Exception e) {
            throw new SystemErrorException("Minio服务出现问题");
        }
    }




}
