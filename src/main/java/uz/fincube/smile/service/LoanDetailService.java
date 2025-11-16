package uz.fincube.smile.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.fincube.smile.model.LoanDetails;
import uz.fincube.smile.repo.LoanDetailRepo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LoanDetailService {

    @Autowired
    private LoanDetailRepo loanDetailRepo;

    public List<LoanDetails> loadCleanAndSort(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        List<LoanDetails> list = new ArrayList<>();

        boolean isHeader = true;
        for (Row row : sheet) {
            if (isHeader) {
                isHeader = false;
                continue;
            }
            LoanDetails r = new LoanDetails();

            r.customerId = parseInt(getCellString(row, 0));
            r.loanType = clean(getCellString(row, 1));
            r.loanAmount = parseMoney(getCellString(row, 2));
            r.loanTerm = parseInt(getCellString(row, 3));
            r.interestRate = parseDouble(getCellString(row, 4));
            r.loanPurpose = clean(getCellString(row, 5));
            r.loanToValueRatio = parseDouble(getCellString(row, 6));
            r.originationChannel = clean(getCellString(row, 7));
            r.loanOfficerId = parseInt(getCellString(row, 8));
            r.marketingCampaign = clean(getCellString(row, 9));

            list.add(r);
        }

        workbook.close();
        fis.close();

        list.sort(Comparator.comparingInt(r -> r.customerId));

        loanDetailRepo.saveAll(list);

        return list;
    }

    public void saveToXlsx(List<LoanDetails> list, String output) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("cleaned");

        Row header = sheet.createRow(0);
        String[] cols = {
                "customer_id", "loan_type", "loan_amount", "loan_term", "interest_rate",
                "loan_purpose", "loan_to_value_ratio", "origination_channel", "loan_officer_id", "marketing_campaign"
        };
        for (int i = 0; i < cols.length; i++) header.createCell(i).setCellValue(cols[i]);

        int rowIndex = 1;
        for (LoanDetails r : list) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(r.customerId);
            row.createCell(1).setCellValue(r.loanType);
            row.createCell(2).setCellValue(r.loanAmount);
            row.createCell(3).setCellValue(r.loanTerm);
            row.createCell(4).setCellValue(r.interestRate);
            row.createCell(5).setCellValue(r.loanPurpose);
            row.createCell(6).setCellValue(r.loanToValueRatio);
            row.createCell(7).setCellValue(r.originationChannel);
            row.createCell(8).setCellValue(r.loanOfficerId);
            row.createCell(9).setCellValue(r.marketingCampaign);
        }

        FileOutputStream fos = new FileOutputStream(output);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    private String getCellString(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private int parseInt(String v) {
        try { return (int) Double.parseDouble(v.trim()); }
        catch (Exception e) { return 0; }
    }

    private double parseDouble(String v) {
        try { return Double.parseDouble(v.replace(",", ".").trim()); }
        catch (Exception e) { return 0; }
    }

    private double parseMoney(String v) {
        try {
            return Double.parseDouble(v.replace("$", "").replace(",", "").trim());
        } catch (Exception e) { return 0; }
    }

    private String clean(String v) {
        if (v == null) return "";

        String text = v.toLowerCase().trim();

        return switch (text) {
            case "creditcard", "cc" -> "credit card";
            case "personal loan" -> "personal";
            default -> text;
        };
    }

    public void printStatistics(List<LoanDetails> list) {
        System.out.println("--- File Statistics ---");
        System.out.println("Total records: " + list.size());


// customer_id
        System.out.println("Min customer_id: " + list.stream().mapToInt(r -> r.customerId).min().orElse(0));
        System.out.println("Max customer_id: " + list.stream().mapToInt(r -> r.customerId).max().orElse(0));


// loan_type counts
        System.out.println("Loan types distribution:");
        list.stream().map(r -> r.loanType)
                .distinct()
                .forEach(t -> System.out.println(" " + t + ": " + list.stream().filter(r -> r.loanType.equals(t)).count()));


// loan_amount
        System.out.println("Avg loan_amount: " + list.stream().mapToDouble(r -> r.loanAmount).average().orElse(0));
        System.out.println("Min loan_amount: " + list.stream().mapToDouble(r -> r.loanAmount).min().orElse(0));
        System.out.println("Max loan_amount: " + list.stream().mapToDouble(r -> r.loanAmount).max().orElse(0));


// loan_term
        System.out.println("Avg loan_term: " + list.stream().mapToInt(r -> r.loanTerm).average().orElse(0));
        System.out.println("Min loan_term: " + list.stream().mapToInt(r -> r.loanTerm).min().orElse(0));
        System.out.println("Max loan_term: " + list.stream().mapToInt(r -> r.loanTerm).max().orElse(0));


// interest_rate
        System.out.println("Avg interest_rate: " + list.stream().mapToDouble(r -> r.interestRate).average().orElse(0));
        System.out.println("Min interest_rate: " + list.stream().mapToDouble(r -> r.interestRate).min().orElse(0));
        System.out.println("Max interest_rate: " + list.stream().mapToDouble(r -> r.interestRate).max().orElse(0));


// loan_purpose distribution
        System.out.println("Loan purposes distribution:");
        list.stream().map(r -> r.loanPurpose)
                .distinct()
                .forEach(t -> System.out.println(" " + t + ": " + list.stream().filter(r -> r.loanPurpose.equals(t)).count()));


// LTV
        System.out.println("Avg LTV: " + list.stream().mapToDouble(r -> r.loanToValueRatio).average().orElse(0));
        System.out.println("Min LTV: " + list.stream().mapToDouble(r -> r.loanToValueRatio).min().orElse(0));
        System.out.println("Max LTV: " + list.stream().mapToDouble(r -> r.loanToValueRatio).max().orElse(0));


// origination_channel distribution
        System.out.println("Origination channels:");
        list.stream().map(r -> r.originationChannel)
                .distinct()
                .forEach(t -> System.out.println(" " + t + ": " + list.stream().filter(r -> r.originationChannel.equals(t)).count()));


// loan_officer_id
        System.out.println("Unique loan officers: " + list.stream().map(r -> r.loanOfficerId).distinct().count());


// marketing_campaign distribution
        System.out.println("Marketing campaigns:");
        list.stream().map(r -> r.marketingCampaign)
                .distinct()
                .forEach(t -> System.out.println(" " + t + ": " + list.stream().filter(r -> r.marketingCampaign.equals(t)).count()));
    }
}
