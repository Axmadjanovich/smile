package uz.fincube.smile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.fincube.smile.model.ApplicationMetadata;
import uz.fincube.smile.model.LoanDetails;
import uz.fincube.smile.service.ApplicationMetadataService;
import uz.fincube.smile.service.Demographics;
import uz.fincube.smile.service.FinancialRatios;
import uz.fincube.smile.service.LoanDetailService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootApplication
public class SmileApplication {

    @Autowired
    private Demographics demographics;


    public static void main(String[] args) throws IOException, URISyntaxException {
        SpringApplication.run(SmileApplication.class, args);
        processFinancialRatios();
//        processLoanDetails();

    }

    // В ApplicationMetadataService.java
    public static void processApplicationMetadata() throws IOException {
        ApplicationMetadataService service = new ApplicationMetadataService();
        List<ApplicationMetadata> applicationMetadata = service.loadCleanAndSort("D:\\smile\\src\\main\\resources\\application_metadata.csv");
        //cleaned.forEach(System.out::println);
        //service.saveToFile(applicationMetadata, "D:\\smile\\src\\main\\resources\\cleaned_application_metadata.csv");
        service.printStatistics(applicationMetadata);

    }

    // В LoanDetailService.java
    public static void processLoanDetails() throws IOException {
        LoanDetailService loanDataService = new LoanDetailService();
        List<LoanDetails> loanDetailRecords = loanDataService.loadCleanAndSort("D:\\smile\\src\\main\\resources\\loan_details.xlsx");
        loanDataService.printStatistics(loanDetailRecords);
        //loanDataService.saveToXlsx(loanDetailRecords, "D:\\smile\\src\\main\\resources\\loanDeatailsCleaned.xlsx");

    }

    public static void processFinancialRatios() throws IOException, URISyntaxException {
        FinancialRatios financialRatios = new FinancialRatios();
        financialRatios.read("D:\\smile\\src\\main\\resources\\financial_ratios.jsonl");
        financialRatios.toString();
    }

//    @EventListener(ApplicationStartedEvent.class)
//    public void init() throws IOException, URISyntaxException {
//        demographics.read("src/main/resources/demographics.csv");
//        financialRatios.read("src/main/resources/financial_ratios.jsonl");
//    }



}
