package it.rik.capstoneBE.config.imginlocale;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:./upload/");
    }

    //metodi di autopart service per salvare le foto in locale (1 create, 2 update, 3 delete)


//    1
//    // Gestione del MultipartFile
//    MultipartFile immagine = request.getImmagine();
//        if (immagine != null && !immagine.isEmpty()) {
//        try {
//            String fileName = immagine.getOriginalFilename();
//            Path fileStorageLocation = Paths.get("./upload").toAbsolutePath().normalize();
//            Path targetLocation = fileStorageLocation.resolve(fileName);
//            Files.copy(immagine.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            autopart.setImmagine(fileName);
//        } catch (IOException ex) {
//            throw new RuntimeException("Errore durante il salvataggio del file", ex);
//        }
//    }
//


//
//    2
//            // Gestione immagine (se presente)
//            if (request.getImmagine() != null && !request.getImmagine().isEmpty()) {
//        try {
//            String fileName = request.getImmagine().getOriginalFilename();
//            Path fileStorageLocation = Paths.get("./upload").toAbsolutePath().normalize();
//            Files.createDirectories(fileStorageLocation); // Crea la directory se non esiste
//            Path targetLocation = fileStorageLocation.resolve(fileName);
//
//            // Cancella l'immagine vecchia se esiste e se è diversa dalla nuova
//            if (autopart.getImmagine() != null && !autopart.getImmagine().equals(fileName)) {
//                Path oldImagePath = fileStorageLocation.resolve(autopart.getImmagine());
//                Files.deleteIfExists(oldImagePath);
//            }
//
//            Files.copy(request.getImmagine().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            autopart.setImmagine(fileName);
//        } catch (IOException ex) {
//            throw new RuntimeException("Errore durante l'aggiornamento del file", ex);
//        }
//


//    3
//        // Cancella l'immagine associata se esiste
//        if (autopart.getImmagine() != null) {
//            Path fileStorageLocation = Paths.get("./upload").toAbsolutePath().normalize();
//            Path imagePath = fileStorageLocation.resolve(autopart.getImmagine());
//            try {
//                Files.deleteIfExists(imagePath);
//            } catch (IOException ex) {
//                throw new RuntimeException("Errore durante la cancellazione del file", ex);
//            }}

}