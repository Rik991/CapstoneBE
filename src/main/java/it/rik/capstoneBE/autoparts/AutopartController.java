// AutopartController.java
package it.rik.capstoneBE.autoparts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autoparts")
public class AutopartController {

    @Autowired
    private AutopartService autopartService;

    @PostMapping
    public ResponseEntity<Autopart> createAutopartWithPrice(@RequestBody AutopartPriceRequestDTO request) {
        Autopart createdAutopart = autopartService.createAutopartWithPrice(request);
        return ResponseEntity.ok(createdAutopart);
    }


    @GetMapping
    public ResponseEntity<List<AutopartResponseDTO>> getAllAutoparts() {
        List<AutopartResponseDTO> autoparts = autopartService.getAllAutoparts();
        return ResponseEntity.ok(autoparts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutopartResponseDTO> getAutopartById(@PathVariable Long id) {
        AutopartResponseDTO autopart = autopartService.getAutopartById(id);
        return ResponseEntity.ok(autopart);
    }

    //autopart by reseller id
    @GetMapping("/reseller/{id}")
    public ResponseEntity<List<AutopartResponseDTO>> getAutopartsByResellerId(@PathVariable Long id) {
        List<AutopartResponseDTO> autoparts = autopartService.getAutopartsByResellerId(id);
        return ResponseEntity.ok(autoparts);
    }
}