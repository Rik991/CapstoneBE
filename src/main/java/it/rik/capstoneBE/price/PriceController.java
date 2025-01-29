package it.rik.capstoneBE.price;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;


    @GetMapping
    public ResponseEntity<List<Price>> getAllPrices() {
        return ResponseEntity.ok(priceService.getAllPrices());
    }

}
