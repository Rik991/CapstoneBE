//package it.rik.capstoneBE.filestorage;
//
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.UUID;
//
//@Service
//@Slf4j
//public class FileStorageService {
//    private final Path fileStorageLocation;
//
//    @Autowired
//    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
//        this.fileStorageLocation = Paths.get(uploadDir)
//                .toAbsolutePath().normalize();
//
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
//        }
//    }
//
//    public String storeFile(MultipartFile file) {
//        // Valida il file
//        if (file == null || file.isEmpty()) {
//            throw new RuntimeException("Invalid file");
//        }
//
//        if (!file.getContentType().startsWith("image/")) {
//            throw new RuntimeException("Only image files are allowed");
//        }
//
//        // Genera nome file univoco
//        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
//        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
//        String fileName = UUID.randomUUID().toString() + "." + fileExtension;
//
//        try {
//            // Verifica path traversal
//            if (fileName.contains("..")) {
//                throw new RuntimeException("Invalid file path sequence");
//            }
//
//            // Salva il file
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//            return fileName;
//        } catch (IOException ex) {
//            throw new RuntimeException("Could not store file " + fileName, ex);
//        }
//    }
//
//    public Resource loadFileAsResource(String fileName) {
//        try {
//            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if(resource.exists()) {
//                return resource;
//            } else {
//                throw new RuntimeException("File not found " + fileName);
//            }
//        } catch (Exception ex) {
//            throw new RuntimeException("File not found " + fileName, ex);
//        }
//    }
//}