package it.rik.capstoneBE.config.amazons3;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    Path loadFile(String fileName);
    void deleteFile(String fileName);
}