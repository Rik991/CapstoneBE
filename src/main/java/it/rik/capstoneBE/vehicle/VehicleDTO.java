package it.rik.capstoneBE.vehicle;

// VehicleDTO.java


import lombok.Data;

@Data
public class VehicleDTO {
    private Long id;
    private String marca;
    private String modello;
    private String tipoMotore;
    private String carrozzeria;
    private int inizioProduzione;
    private int fineProduzione;
}