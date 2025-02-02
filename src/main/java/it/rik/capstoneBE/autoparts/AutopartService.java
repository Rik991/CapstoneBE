// AutopartService.java
package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.config.amazons3.FileStorageService;
import it.rik.capstoneBE.exceptions.NotYourAutopart;
import it.rik.capstoneBE.mapper.Mapper;
import it.rik.capstoneBE.price.Prezzo;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import it.rik.capstoneBE.vehicle.Vehicle;
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
    private final FileStorageService fileStorageServiceAmazon;

    public Page<AutopartDTO.Response> getAllAutoparts(Pageable pageable) {
        return autopartRepository.findAll(pageable)
                .map(mapper::mapToResponse);
    }

    public AutopartDTO.Response getAutopartById(Long id, String username) {
        Autopart autopart = autopartRepository.findByIdWithDetails(id).orElseThrow(()-> new EntityNotFoundException("Ricambio non trovato"));

        //controlliamo sempre che il reseller sia presente e che il ricambio sia il suo
        Reseller reseller = resellerRepository.findByUserUsername(username).orElseThrow(()-> new EntityNotFoundException("Venditore non trovato"));
        if (!autopart.getReseller().getId().equals(reseller.getId())){
            throw new NotYourAutopart("Il ricambio selezionato non è tuo");
        }

        return mapper.mapToResponse(autopart);
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
            String fileName = fileStorageServiceAmazon.storeFile(immagine);
            autopart.setImmagine(fileName);
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

    @Transactional
    public AutopartDTO.Response updateAutopart(Long id, AutopartDTO.Request request, String username ){

        Autopart autopart = autopartRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ricambio non trovato"));

        // Controlliamo per sicurezza se c'è il reseller e se è il proprietario di quel ricambio
        Reseller reseller = resellerRepository.findByUserUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Reseller non trovato"));
        if (!autopart.getReseller().getId().equals(reseller.getId())) {
            throw new NotYourAutopart("Non sei autorizzato a modificare questa autopart");
        }

        //Aggiornamento dei campi
        autopart.setNome(request.getNome());
        autopart.setCodiceOe(request.getCodiceOe());
        autopart.setDescrizione(request.getDescrizione());
        autopart.setCategoria(request.getCategoria());
        autopart.setCondizione(request.getCondizione());


        // Gestione dell'immagine
        MultipartFile nuovaImmagine = request.getImmagine();
        if (nuovaImmagine != null && !nuovaImmagine.isEmpty()) {
            // Se esiste una vecchia immagine e il nome è diverso dalla nuova, cancellala dal bucket S3
            if (autopart.getImmagine() != null && !autopart.getImmagine().equals(nuovaImmagine.getOriginalFilename())) {
                try {
                    fileStorageServiceAmazon.deleteFile(autopart.getImmagine());
                } catch (Exception ex) {
                    System.err.println("Errore nella cancellazione del file vecchio: " + ex.getMessage());
                }
            }
            // Carica la nuova immagine su S3 e aggiorna il campo dell'entità
            String nuovoNomeFile = fileStorageServiceAmazon.storeFile(nuovaImmagine);
            autopart.setImmagine(nuovoNomeFile);
        }
        //Aggiorniamo i veicoli compatibili
        Set<Vehicle> veicoliAggiornati = new HashSet<>(vehicleRepository.findAllById(request.getVeicoliIds()));
        autopart.setVeicoliCompatibili(veicoliAggiornati);

        // Aggiorniamo il prezzo, così facendo però non avrò lo storico, in caso va creato un nuovo prezzo come nella create
        Optional<Prezzo> ultimoPrezzo = autopart.getPrezzi().stream()
                .max(Comparator.comparing(Prezzo::getDataInserimento));

        if (ultimoPrezzo.isPresent()) {
            ultimoPrezzo.get().setImporto(request.getPrezzo());
        } else {
            Prezzo prezzo = new Prezzo();
            prezzo.setImporto(request.getPrezzo());
            prezzo.setAutopart(autopart);
            prezzo.setReseller(reseller);
            autopart.getPrezzi().add(prezzo);
        }

        return mapper.mapToResponse(autopartRepository.save(autopart));

    }

    @Transactional
    public void deleteAutopart (Long id, String username){
        Autopart autopart = autopartRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ricambio non trovato"));

        //controlliamo sempre che il reseller sia presente e che il ricambio sia il suo
        Reseller reseller = resellerRepository.findByUserUsername(username).orElseThrow(()-> new EntityNotFoundException("Venditore non trovato"));
        if (!autopart.getReseller().getId().equals(reseller.getId())){
            throw new SecurityException("Non sei autorizzato ad eliminare questo ricambio");
        }

        // Cancella l'immagine associata, se esiste
        if (autopart.getImmagine() != null) {
            try {
                fileStorageServiceAmazon.deleteFile(autopart.getImmagine());
            } catch (Exception ex) {
                // Logga l'errore o gestisci l'eccezione in base alle tue esigenze
                System.err.println("Errore nella cancellazione del file: " + ex.getMessage());
            }
        }
        autopartRepository.delete(autopart);
    }


    public Page<AutopartDTO.Response> findByVehicle(Long vehicleId, Pageable pageable) {
        return autopartRepository.findByVeicoliCompatibiliId(vehicleId, pageable)
                .map(mapper::mapToResponse);
    }

    public Page<AutopartDTO.Response> findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return autopartRepository.findByPrezziImportoBetween(minPrice, maxPrice, pageable)
                .map(mapper::mapToResponse);
    }

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
            String searchWords,
            Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            searchWords = "%" + search.replace(" ", "%") + "%";
        }
        Page<Autopart> autopartsPage = autopartRepository.search(
                codiceOe, categoria, marca, modello, minPrezzo, maxPrezzo, condizione, search, searchWords, pageable
        );
        return autopartsPage.map(autopart -> mapper.mapToResponse(autopart));
    }


}