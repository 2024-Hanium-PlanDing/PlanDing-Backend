package com.tukorea.planding.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "fcm")
public class FireBaseProperties {
    private String serviceAccountFile;
}
