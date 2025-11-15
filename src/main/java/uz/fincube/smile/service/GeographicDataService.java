package uz.fincube.smile.service;

import jakarta.xml.bind.*;
import org.springframework.stereotype.Service;
import uz.fincube.smile.model.GeographicCustomer;
import uz.fincube.smile.model.GeographicData;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeographicDataService {

    public GeographicData loadFromXml(String filePath) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(GeographicData.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        return (GeographicData) unmarshaller.unmarshal(new File(filePath));
    }

    private GeographicCustomer clean(GeographicCustomer c) {

        c.setState(c.getState() == null ? "" : c.getState().trim().toUpperCase());

        if (c.getRegionalUnemploymentRate() < 0) c.setRegionalUnemploymentRate(0);
        if (c.getRegionalMedianIncome() < 0) c.setRegionalMedianIncome(0);
        if (c.getRegionalMedianRent() < 0) c.setRegionalMedianRent(0);
        if (c.getHousingPriceIndex()< 0) c.setHousingPriceIndex(0);
        if (c.getCostOfLivingIndex() < 0) c.setCostOfLivingIndex(0);

        String zip = String.valueOf(c.getPreviousZipCode());
        if (zip.length() < 3)
            c.setPreviousZipCode(String.valueOf(0));

        return c;
    }

    public List<GeographicCustomer> cleanAndSort(GeographicData data) {
        return data.customers.stream()
                .map(this::clean)
                .sorted(Comparator.comparingInt(c -> c.getId()))
                .collect(Collectors.toList());
    }

    public void printStatistics(List<GeographicCustomer> list) {

        System.out.println("------ Geographic Data Statistics ------");
        System.out.println("Total customers: " + list.size());

        System.out.println("State distribution:");
        list.stream()
                .map(c -> c.getState())
                .distinct()
                .forEach(state ->
                        System.out.println("  " + state + ": " +
                                list.stream().filter(c -> c.getState().equals(state)).count())
                );

        System.out.println("Avg unemployment rate: " +
                list.stream().mapToDouble(c -> c.getRegionalUnemploymentRate()).average().orElse(0));

        System.out.println("Avg median income: " +
                list.stream().mapToInt(c -> c.getRegionalMedianIncome()).average().orElse(0));

        System.out.println("Avg rent: " +
                list.stream().mapToDouble(c -> c.getRegionalMedianRent()).average().orElse(0));
    }

    public void saveCleanedXml(List<GeographicCustomer> customers, String outputPath) throws Exception {
        GeographicData data = new GeographicData();
        data.customers = customers;

        JAXBContext ctx = JAXBContext.newInstance(GeographicData.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(data, new File(outputPath));
    }

    public void processGeographicXml(String input) throws Exception {
        GeographicData data = loadFromXml(input);
        List<GeographicCustomer> cleaned = cleanAndSort(data);
        cleaned.forEach(System.out::println);
        printStatistics(cleaned);
        //saveCleanedXml(cleaned, output);
    }
}
