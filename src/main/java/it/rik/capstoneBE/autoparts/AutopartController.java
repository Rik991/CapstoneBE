// AutopartController.java
package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.filestorage.FileStorageService;
import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autoparts")
@RequiredArgsConstructor
public class AutopartController {

    private final AutopartService autopartService;
    private final ResellerRepository resellerRepository;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<Page<AutopartDTO.Response>> getAll(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable
    ) {
        if (vehicleId != null) {
            return ResponseEntity.ok(autopartService.findByVehicle(vehicleId, pageable));
        } else if (minPrice != null || maxPrice != null) {
            return ResponseEntity.ok(autopartService.findByPriceRange(
                    minPrice != null ? minPrice : 0.0,
                    maxPrice != null ? maxPrice : Double.MAX_VALUE,
                    pageable
            ));
        }
        return ResponseEntity.ok(autopartService.getAllAutoparts(pageable));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AutopartDTO.Response> createAutopart(
            @ModelAttribute AutopartDTO.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(autopartService.createAutopart(request, userDetails.getUsername()));
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RESELLER')")
    public ResponseEntity<AutopartDTO.Response> updateAutopart(
            @PathVariable Long id, @ModelAttribute AutopartDTO.Request request, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(autopartService.updateAutopart(id, request, userDetails.getUsername()));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('RESELLER')")
    public ResponseEntity<Void> deleteAutopart(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        autopartService.deleteAutopart(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('RESELLER')")
    public ResponseEntity<AutopartDTO.Response> getById (@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(autopartService.getAutopartById(id, userDetails.getUsername()));
    }


    @GetMapping("/reseller")
    @PreAuthorize("hasRole('RESELLER')")
    public ResponseEntity<Page<AutopartDTO.Response>> getByReseller(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable
    ) {
        Reseller reseller = resellerRepository.findByUserUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Reseller non trovato"));
        return ResponseEntity.ok(autopartService.getAllAutopartsByResellerId(reseller.getId(), pageable));
    }

    //ebdpoint per filtri di ricerca
    @GetMapping("/search")
    public ResponseEntity<Page<AutopartDTO.Response>> searchAutoparts(
            @RequestParam(required = false) String codiceOe,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modello,
            @RequestParam(required = false) Double minPrezzo,
            @RequestParam(required = false) Double maxPrezzo,
            @RequestParam(required = false) String condizione, // come String
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String searchWord,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // Converte la stringa in enum, se valorizzata e non vuota
        Condizione condizioneEnum = (condizione == null || condizione.trim().isEmpty())
                ? null
                : Condizione.valueOf(condizione.toUpperCase());

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(autopartService.searchAutoparts(
                codiceOe, categoria, marca, modello, minPrezzo, maxPrezzo, condizioneEnum, search, searchWord, pageable));
    }


}