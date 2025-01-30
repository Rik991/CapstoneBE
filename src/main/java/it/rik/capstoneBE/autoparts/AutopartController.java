// AutopartController.java
package it.rik.capstoneBE.autoparts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}