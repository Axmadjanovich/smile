package uz.fincube.smile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import uz.fincube.smile.model.ApplicationMetadata;
import uz.fincube.smile.model.LoanDetails;
import uz.fincube.smile.service.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootApplication
public class SmileApplication {

    @Autowired
    private Demographics demographics;

    @Autowired
    private FinancialRatios financialRatios;

    @Autowired
    private CreditHistory creditHistory;

    @Autowired
    private ApplicationMetadataService applicationMetadataService;

    @Autowired
    private LoanDetailService loanDetailService;

    public static void main(String[] args) throws IOException, URISyntaxException {
        SpringApplication.run(SmileApplication.class, args);
    }

    // В ApplicationMetadataService.java
    public static void processApplicationMetadata() throws IOException {
        ApplicationMetadataService service = new ApplicationMetadataService();
        List<ApplicationMetadata> applicationMetadata = service.loadCleanAndSort("src/main/resources/application_metadata.csv");
        service.printStatistics(applicationMetadata);

    }

    // В LoanDetailService.java
    public static void processLoanDetails() throws IOException {
        LoanDetailService loanDataService = new LoanDetailService();
        List<LoanDetails> loanDetailRecords = loanDataService.loadCleanAndSort("src/main/resources/loan_details.xlsx");
        loanDataService.printStatistics(loanDetailRecords);
        //loanDataService.saveToXlsx(loanDetailRecords, "D:\\smile\\src\\main\\resources\\loanDeatailsCleaned.xlsx");

    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() throws IOException, URISyntaxException {
//        demographics.read("src/main/resources/demographics.csv");
//        financialRatios.read("src/main/resources/financial_ratios.jsonl");
//        creditHistory.read("src/main/resources/credit_history.parquet");
        applicationMetadataService.loadCleanAndSort("src/main/resources/application_metadata.csv");
        loanDetailService.loadCleanAndSort("src/main/resources/loan_details.xlsx");
    }

}
