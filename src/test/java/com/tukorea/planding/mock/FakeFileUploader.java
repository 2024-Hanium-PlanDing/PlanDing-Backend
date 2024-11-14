package com.tukorea.planding.mock;

import com.tukorea.planding.global.config.s3.FileUploader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FakeFileUploader implements FileUploader {
    @Override
    public String upload(MultipartFile file, String dirName) throws IOException {
        return null;
    }

    @Override
    public String uploadProfileImage(MultipartFile file) throws IOException {
        return null;
    }

    @Override
    public String uploadGroupThumbnail(MultipartFile file) throws IOException {
        return null;
    }
}
