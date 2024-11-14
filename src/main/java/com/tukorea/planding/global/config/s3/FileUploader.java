package com.tukorea.planding.global.config.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {
    String upload(MultipartFile file, String dirName) throws IOException;

    String uploadProfileImage(MultipartFile file) throws IOException;

    String uploadGroupThumbnail(MultipartFile file) throws IOException;
}
