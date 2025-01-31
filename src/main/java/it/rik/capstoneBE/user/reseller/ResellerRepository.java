package it.rik.capstoneBE.user.reseller;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResellerRepository extends JpaRepository<Reseller, Long> {

    //findbyusername

    Optional<Reseller> findByUserUsername(String username);

}