package it.rik.capstoneBE.prestashop;

import it.rik.capstoneBE.autoparts.Autopart;
import lombok.Data;

import java.util.List;

@Data
public class ApiProductPage {
    private List<Autopart> content;
    private long totalElements;
}