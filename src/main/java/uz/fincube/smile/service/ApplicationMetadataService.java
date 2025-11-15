package uz.fincube.smile.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import smile.data.DataFrame;
import smile.data.vector.DoubleVector;
import smile.io.Read;
import uz.fincube.smile.model.ApplicationMetadata;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationMetadataService {

    public List<ApplicationMetadata> loadCleanAndSort(String filePath) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filePath));

        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreSurroundingSpaces()
                .withTrim()
        );

        List<ApplicationMetadata> records = parser.getRecords()
                .stream()
                .map(this::mapRecord)
                .collect(Collectors.toList());

        parser.close();

        // СОРТИРОВКА (например, по customerRef)
        return records.stream()
                .sorted(Comparator.comparingInt(r -> r.customerRef))
                .collect(Collectors.toList());
    }

    private ApplicationMetadata mapRecord(CSVRecord row) {
        ApplicationMetadata r = new ApplicationMetadata();
        r.customerRef = parseInt(row.get("customer_ref"));
        r.applicationId = parseInt(row.get("application_id"));
        r.applicationHour = parseInt(row.get("application_hour"));
        r.applicationDayOfWeek = parseInt(row.get("application_day_of_week"));
        r.accountOpenYear = parseInt(row.get("account_open_year"));

        r.preferredContact = cleanString(row.get("preferred_contact"));
        r.referralCode = cleanString(row.get("referral_code"));
        r.accountStatusCode = cleanString(row.get("account_status_code"));

        r.randomNoise1 = parseDouble(row.get("random_noise_1"));
        r.numLoginSessions = parseInt(row.get("num_login_sessions"));
        r.numCustomerServiceCalls = parseInt(row.get("num_customer_service_calls"));

        r.hasMobileApp = parseBoolean(row.get("has_mobile_app"));
        r.paperlessBilling = parseBoolean(row.get("paperless_billing"));
        r.defaultFlag = parseInt(row.get("default"));

        return r;
    }


    public void saveToFile(List<ApplicationMetadata> records, String outputPath) throws IOException {

        FileWriter writer = new FileWriter(outputPath);

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader(
                        "customer_ref",
                        "application_id",
                        "application_hour",
                        "application_day_of_week",
                        "account_open_year",
                        "preferred_contact",
                        "referral_code",
                        "account_status_code",
                        "random_noise_1",
                        "num_login_sessions",
                        "num_customer_service_calls",
                        "has_mobile_app",
                        "paperless_billing",
                        "default"
                )
        );

        for (ApplicationMetadata r : records) {
            csvPrinter.printRecord(
                    r.customerRef,
                    r.applicationId,
                    r.applicationHour,
                    r.applicationDayOfWeek,
                    r.accountOpenYear,
                    r.preferredContact,
                    r.referralCode,
                    r.accountStatusCode,
                    r.randomNoise1,
                    r.numLoginSessions,
                    r.numCustomerServiceCalls,
                    r.hasMobileApp ? 1 : 0,
                    r.paperlessBilling ? 1 : 0,
                    r.defaultFlag
            );
        }

        csvPrinter.flush();
        csvPrinter.close();
    }

    private int parseInt(String v) {
        try { return Integer.parseInt(v.trim()); }
        catch (Exception e) { return 0; }
    }

    private double parseDouble(String v) {
        try { return Double.parseDouble(v.trim()); }
        catch (Exception e) { return 0.0; }
    }

    private boolean parseBoolean(String v) {
        return "1".equals(v.trim()) || v.equalsIgnoreCase("true");
    }

    private String cleanString(String v) {
        if (v == null) return "";
        return v.trim().replaceAll("[^a-zA-Z0-9\\-]", "");
    }

    public void printStatistics(List<ApplicationMetadata> records) {
        System.out.println("--- Statistics for Application Metadata ---");
        System.out.println("Total records: " + records.size());


// Numeric columns
        System.out.println("customerRef: min=" + records.stream().mapToInt(r -> r.customerRef).min().orElse(0)
                + ", max=" + records.stream().mapToInt(r -> r.customerRef).max().orElse(0));
        System.out.println("applicationId: min=" + records.stream().mapToInt(r -> r.applicationId).min().orElse(0)
                + ", max=" + records.stream().mapToInt(r -> r.applicationId).max().orElse(0));
        System.out.println("applicationHour: avg=" + records.stream().mapToInt(r -> r.applicationHour).average().orElse(0));
        System.out.println("applicationDayOfWeek: avg=" + records.stream().mapToInt(r -> r.applicationDayOfWeek).average().orElse(0));
        System.out.println("accountOpenYear: min=" + records.stream().mapToInt(r -> r.accountOpenYear).min().orElse(0)
                + ", max=" + records.stream().mapToInt(r -> r.accountOpenYear).max().orElse(0));
        System.out.println("randomNoise1: avg=" + records.stream().mapToDouble(r -> r.randomNoise1).average().orElse(0));
        System.out.println("numLoginSessions: avg=" + records.stream().mapToInt(r -> r.numLoginSessions).average().orElse(0));
        System.out.println("numCustomerServiceCalls: avg=" + records.stream().mapToInt(r -> r.numCustomerServiceCalls).average().orElse(0));


// Boolean columns
        System.out.println("hasMobileApp: count=" + records.stream().filter(r -> r.hasMobileApp).count());
        System.out.println("paperlessBilling: count=" + records.stream().filter(r -> r.paperlessBilling).count());


// Categorical columns
        System.out.println("preferredContact distribution:");
        records.stream().map(r -> r.preferredContact).distinct()
                .forEach(v -> System.out.println(" " + v + ": " + records.stream().filter(r -> r.preferredContact.equals(v)).count()));
        System.out.println("referralCode distribution:");
        records.stream().map(r -> r.referralCode).distinct()
                .forEach(v -> System.out.println(" " + v + ": " + records.stream().filter(r -> r.referralCode.equals(v)).count()));
        System.out.println("accountStatusCode distribution:");
        records.stream().map(r -> r.accountStatusCode).distinct()
                .forEach(v -> System.out.println(" " + v + ": " + records.stream().filter(r -> r.accountStatusCode.equals(v)).count()));


        System.out.println("defaultFlag: count=" + records.stream().filter(r -> r.defaultFlag == 1).count());
    }
}


