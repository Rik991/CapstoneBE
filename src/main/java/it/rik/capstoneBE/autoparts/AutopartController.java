package it.rik.capstoneBE.autoparts;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/autoparts")
public class AutopartController {

    @Autowired
    private AutopartService autopartService;

    public ResponseEntity<List<Autopart>> getAllAutoparts() {
        return ResponseEntity.ok(autopartService.getAllAutoparts());
    }
}
