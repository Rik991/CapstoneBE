package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.price.PrezzoInfo;
import it.rik.capstoneBE.user.reseller.ResellerInfo;
import it.rik.capstoneBE.vehicle.VehicleInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;


public class AutopartDTO {
    @Data
    public static class Response {
        private Long id;
        private String nome;
        private String codiceOe;
        private String descrizione;
        private String categoria;
        private Condizione condizione;
        private String immagine;
        private List<VehicleInfo> veicoliCompatibili;
        private List<PrezzoInfo> prezzi;
        private ResellerInfo reseller;
        private Double ratingMedio;
    }

    @Data
    public static class Request {
        @NotBlank
        private String nome;
        @NotBlank private String codiceOe;
        private String descrizione;
        private String categoria;
        private Condizione condizione;
        private MultipartFile immagine;
        @Positive
        @NotNull
        private Double prezzo;
        @NotEmpty
        private Set<Long> veicoliIds;
    }


}