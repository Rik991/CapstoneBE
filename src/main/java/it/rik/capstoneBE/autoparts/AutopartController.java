// AutopartController.java
package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @PreAuthorize("hasRole('RESELLER')")
    public ResponseEntity<AutopartDTO.Response> createAutopart(
            @Valid @RequestBody AutopartDTO.Request request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(autopartService.createAutopart(request, username));
    }
    @GetMapping("/reseller/{resellerId}")
    public ResponseEntity<Page<AutopartDTO.Response>> getByReseller(
            @PathVariable Long resellerId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(autopartService.getAllAutopartsByResellerId(resellerId, pageable));
    }

}