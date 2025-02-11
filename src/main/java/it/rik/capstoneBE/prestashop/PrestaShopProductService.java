package it.rik.capstoneBE.prestashop;

import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.autoparts.Condizione;
import it.rik.capstoneBE.price.Prezzo;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrestaShopProductService {

    private final RestTemplate restTemplate;
    private final ResellerRepository resellerRepository; // Iniettato

    // Inserisci la chiave API e l'URL base del tuo PrestaShop
    private final String wsKey = "4HK9QL4EL4WR3TLL954V4M8X23DHFCLP"; // Assicurati che sia valorizzata
    private final String baseUrl = "https://www.perinettiservice.com/api/";

    public PrestaShopProductService(RestTemplate restTemplate, ResellerRepository resellerRepository) {
        this.restTemplate = restTemplate;
        this.resellerRepository = resellerRepository;
    }

    public List<Autopart> getProducts(int page, int size) throws Exception {
        int offset = (page - 1) * size;
        // Formato per PrestaShop: limit=start,number
        String listUrl = baseUrl + "products?ws_key=" + wsKey + "&output_format=XML&limit=" + offset + "," + size;
        String listXml = restTemplate.getForObject(listUrl, String.class);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document listDoc = builder.parse(new InputSource(new StringReader(listXml)));
        listDoc.getDocumentElement().normalize();

        NodeList productNodes = listDoc.getElementsByTagName("product");
        List<Autopart> autoparts = new ArrayList<>();

        for (int i = 0; i < productNodes.getLength(); i++) {
            Element productElement = (Element) productNodes.item(i);
            String detailUrl = productElement.getAttribute("xlink:href") + "?ws_key=" + wsKey;
            String detailXml = restTemplate.getForObject(detailUrl, String.class);
            Autopart autopart = convertProductXmlToAutopart(detailXml);
            autoparts.add(autopart);
        }
        return autoparts;
    }

    private Autopart convertProductXmlToAutopart(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
        doc.getDocumentElement().normalize();

        Autopart autopart = new Autopart();

        // 1. Estrai il nome
        NodeList nameNodes = doc.getElementsByTagName("name");
        if (nameNodes.getLength() > 0) {
            Element nameElement = (Element) nameNodes.item(0);
            NodeList langNodes = nameElement.getElementsByTagName("language");
            if (langNodes.getLength() > 0) {
                String nome = langNodes.item(0).getTextContent().trim();
                autopart.setNome(nome);
            }
        }

        // 2. Estrai il codice OEM
        NodeList mpnNodes = doc.getElementsByTagName("mpn");
        if (mpnNodes.getLength() > 0) {
            String codiceOe = mpnNodes.item(0).getTextContent().trim();
            autopart.setCodiceOe(codiceOe);
        }

        // 3. Estrai la descrizione
        NodeList descNodes = doc.getElementsByTagName("description");
        if (descNodes.getLength() > 0) {
            Element descElement = (Element) descNodes.item(0);
            NodeList langNodes = descElement.getElementsByTagName("language");
            if (langNodes.getLength() > 0) {
                String descrizione = langNodes.item(0).getTextContent().trim();
                autopart.setDescrizione(descrizione);
            }
        }

        // 4. Estrai la categoria
        NodeList catNodes = doc.getElementsByTagName("id_category_default");
        if (catNodes.getLength() > 0) {
            String categoria = catNodes.item(0).getTextContent().trim();
            autopart.setCategoria(categoria);
        }

        // 5. Estrai la condizione
        NodeList condNodes = doc.getElementsByTagName("condition");
        if (condNodes.getLength() > 0) {
            String condStr = condNodes.item(0).getTextContent().trim();
            try {
                autopart.setCondizione(Condizione.valueOf(condStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                autopart.setCondizione(null);
            }
        }

        // 6. Estrai l'immagine
        NodeList imgNodes = doc.getElementsByTagName("id_default_image");
        if (imgNodes.getLength() > 0) {
            Element imgElement = (Element) imgNodes.item(0);
            String immagineUrl = imgElement.getAttribute("xlink:href").trim();
            autopart.setImmagine(immagineUrl);
        }

        // 7. Estrai il prezzo
        NodeList priceNodes = doc.getElementsByTagName("price");
        if (priceNodes.getLength() > 0) {
            String priceStr = priceNodes.item(0).getTextContent().trim();
            Prezzo prezzo = new Prezzo();
            prezzo.setImporto(Double.valueOf(priceStr));
            prezzo.setAutopart(autopart);
            autopart.getPrezzi().add(prezzo);
        }

        // --- Assegna un reseller di default se non presente o se il suo id è null ---
        if (autopart.getReseller() == null || autopart.getReseller().getId() == null) {
            // Recupera il reseller di default, per esempio quello con id = 3 (che corrisponde a reseller1)
            Reseller defaultReseller = resellerRepository.findById(3L)
                    .orElseThrow(() -> new EntityNotFoundException("Default reseller non trovato"));
            autopart.setReseller(defaultReseller);
        }

        return autopart;
    }
}
