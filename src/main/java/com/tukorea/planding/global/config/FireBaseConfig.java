//package com.tukorea.planding.global.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class FireBaseConfig {
//
//    @Value("${fcm.service-account-file}")
//    private String serviceAccountFilePath;
//
//
//    @PostConstruct
//    public void initialize() throws IOException {
//        FileInputStream serviceAccount = new FileInputStream(serviceAccountFilePath);
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//
//        FirebaseApp.initializeApp(options);
//    }
//
//}
