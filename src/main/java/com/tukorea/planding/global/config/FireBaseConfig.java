package com.tukorea.planding.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FireBaseConfig {

    private final FireBaseProperties fireBaseProperties;

    @PostConstruct
    public void initialize() throws IOException {
        ClassPathResource serviceAccountResource = new ClassPathResource(fireBaseProperties.getServiceAccountFile());
        try (InputStream serviceAccountStream = serviceAccountResource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error("Failed to initialize Firebase", e);
            throw e;
        }
    }

}
