// AutopartService.java
package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.mapper.Mapper;
import it.rik.capstoneBE.price.Prezzo;
import it.rik.capstoneBE.price.PrezzoInfo;
import it.rik.capstoneBE.rating.RatingRepository;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerInfo;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import it.rik.capstoneBE.vehicle.Vehicle;
import it.rik.capstoneBE.vehicle.VehicleInfo;
import it.rik.capstoneBE.vehicle.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutopartService {

    private final AutopartRepository autopartRepository;
    private final VehicleRepository vehicleRepository;
    private final ResellerRepository resellerRepository;
    private final Mapper mapper;

    public Page<AutopartDTO.Response> getAllAutoparts(Pageable pageable) {
        return autopartRepository.findAll(pageable)
                .map(mapper::mapToResponse);
    }

    public AutopartDTO.Response getAutopartById(Long id) {
        return autopartRepository.findByIdWithDetails(id)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Autopart non trovata"));
    }

    @Transactional
    public AutopartDTO.Response createAutopart(AutopartDTO.Request request, String username) {
        Reseller reseller = null;
        if (username != null) {
            reseller = resellerRepository.findByUserUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Reseller non trovato"));
        }

        Autopart autopart = new Autopart();
        autopart.setReseller(reseller);
        autopart.setNome(request.getNome());
        autopart.setCodiceOe(request.getCodiceOe());
        autopart.setDescrizione(request.getDescrizione());
        autopart.setCategoria(request.getCategoria());
        autopart.setCondizione(request.getCondizione());

        // Gestione del MultipartFile
        MultipartFile immagine = request.getImmagine();
        if (immagine != null && !immagine.isEmpty()) {
            try {
                String fileName = immagine.getOriginalFilename();
                Path fileStorageLocation = Paths.get("./upload").toAbsolutePath().normalize();
                Path targetLocation = fileStorageLocation.resolve(fileName);
                Files.copy(immagine.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                autopart.setImmagine(fileName);
            } catch (IOException ex) {
                throw new RuntimeException("Errore durante il salvataggio del file", ex);
            }
        }

        if (reseller != null) {
            autopart.setReseller(reseller);
        }

        Set<Vehicle> vehicles = new HashSet<>(vehicleRepository.findAllById(request.getVeicoliIds()));
        autopart.setVeicoliCompatibili(vehicles);

        Prezzo prezzo = new Prezzo();
        prezzo.setImporto(request.getPrezzo());
        prezzo.setAutopart(autopart);
        if (reseller != null) {
            prezzo.setReseller(reseller);
        }
        autopart.getPrezzi().add(prezzo);

        return mapper.mapToResponse(autopartRepository.save(autopart));
    }


    public Page<AutopartDTO.Response> findByVehicle(Long vehicleId, Pageable pageable) {
        return autopartRepository.findByVeicoliCompatibiliId(vehicleId, pageable)
                .map(mapper::mapToResponse);
    }

    public Page<AutopartDTO.Response> findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return autopartRepository.findByPrezziImportoBetween(minPrice, maxPrice, pageable)
                .map(mapper::mapToResponse);
    }
    // AutopartService.java
    public Page<AutopartDTO.Response> getAllAutopartsByResellerId(Long resellerId, Pageable pageable) {
        return autopartRepository.findByResellerId(resellerId, pageable)
                .map(mapper::mapToResponse);
    }
    public Page<AutopartDTO.Response> searchAutoparts(
            String codiceOe,
            String categoria,
            String marca,
            String modello,
            Double minPrezzo,
            Double maxPrezzo,
            Condizione condizione,
            String search,
            Pageable pageable) {
        Page<Autopart> autopartsPage = autopartRepository.search(
                codiceOe, categoria, marca, modello, minPrezzo, maxPrezzo, condizione, search, pageable
        );
        return autopartsPage.map(autopart -> mapper.mapToResponse(autopart));
    }
}