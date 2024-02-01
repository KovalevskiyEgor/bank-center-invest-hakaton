package com.bank.props;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {
    private String bucket;
    private String url;
    private String accessKey;
    private String secretKey;
}