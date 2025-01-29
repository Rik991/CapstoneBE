package it.rik.capstoneBE.autoparts;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/autoparts")
public class AutopartController {

    @Autowired
    private AutopartService autopartService;

    @GetMapping
    public ResponseEntity<List<AutopartDTO>> getAllAutoparts() {
        return ResponseEntity.ok(autopartService.getAllAutoparts());
    }
}
