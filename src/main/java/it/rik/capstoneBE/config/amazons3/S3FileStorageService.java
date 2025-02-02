package it.rik.capstoneBE.config.amazons3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;


@Service
public class S3FileStorageService implements FileStorageService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public S3FileStorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Errore durante il caricamento del file su S3: " + fileName, ex);
        }
    }

    @Override
    public Path loadFile(String fileName) {
        throw new UnsupportedOperationException("Load file non implementato per S3");
    }

    @Override
    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }
}
