package it.rik.capstoneBE.user.reseller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResellerService {

    @Autowired
    private ResellerRepository resellerRepository;

    @Transactional(readOnly = true)
    public List<Reseller> getAll(){
       return resellerRepository.findAll();
    }

}
