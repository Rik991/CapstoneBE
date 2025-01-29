package it.rik.capstoneBE.vehicle;

import lombok.Data;

import java.util.Set;

@Data
public class VehicleDTO {
    private Long id;
    private String marca;
    private String modello;
    private Set<String> tipiMotore;
    private String carrozzeria;
    private int inizioProduzione;
    private int fineProduzione;
}