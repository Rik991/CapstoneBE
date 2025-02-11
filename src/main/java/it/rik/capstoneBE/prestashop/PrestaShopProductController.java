package it.rik.capstoneBE.prestashop;

import it.rik.capstoneBE.autoparts.Autopart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prestashop")
@RequiredArgsConstructor
public class PrestaShopProductController {

    private final PrestaShopProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Autopart>> getPrestaShopProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<Autopart> products = productService.getProducts(page, size);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}