package it.rik.capstoneBE.autoparts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutopartService {

    @Autowired
    private AutopartRepository autopartRepository;

    public List<AutopartDTO> getAllAutoparts() {
        return autopartRepository.findAllWithPriceAndReseller();
    }
}
