package uz.fincube.smile;

import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import smile.data.DataFrame;
import smile.data.Dataset;
//import smile.data.Row;
import smile.data.vector.DoubleVector;
import smile.io.Read;
import uz.fincube.smile.service.Demographics;
import uz.fincube.smile.service.FinancialRatios;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class SmileApplication {

    @Autowired
    private Demographics demographics;

    @Autowired
    private FinancialRatios financialRatios;

    public static void main(String[] args) throws IOException, URISyntaxException {
        SpringApplication.run(SmileApplication.class, args);

    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() throws IOException, URISyntaxException {
        demographics.read("src/main/resources/demographics.csv");
        financialRatios.read("src/main/resources/financial_ratios.jsonl");
    }


}
