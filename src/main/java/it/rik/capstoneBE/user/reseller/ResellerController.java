package it.rik.capstoneBE.user.reseller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reseller")
public class ResellerController {

    @Autowired
    private ResellerService resellerService;


    @GetMapping
    public ResponseEntity<List<Reseller>> getAll() {
        List<Reseller> resellerList = resellerService.getAll();
        return ResponseEntity.ok(resellerList);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Reseller> getResellerByUserId(@PathVariable Long userId) {
        Reseller reseller = resellerService.getResellerByUserId(userId);
        return ResponseEntity.ok(reseller);
    }

}
