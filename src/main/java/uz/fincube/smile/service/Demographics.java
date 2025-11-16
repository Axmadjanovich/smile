package uz.fincube.smile.service;

import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import smile.data.DataFrame;
import smile.data.vector.DoubleVector;
import smile.data.vector.StringVector;
import smile.io.Read;
import uz.fincube.smile.entity.Demography;
import uz.fincube.smile.repo.DemographyRepo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Demographics {

    @Autowired
    private DemographyRepo demographyRepo;

    public void read(String path) throws IOException, URISyntaxException {

        DataFrame df = Read.csv(path, CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(','));

        df = annualIncome(df);
        df = employmentLength(df);
        df = employmentType(df);
        df = education(df);
        df = maritalStatus(df);

        // eski string ustunni o‘chirish

        System.out.println("\n\n\n\n\n================================================DEMOGRAPHICS================================================\n\n\n\n\n");

        System.out.println(df.structure());
        System.out.println(df.summary());

        insertDb(df);
    }

    public DataFrame annualIncome(DataFrame df){
        String[] incomeStr = df.stringVector("annual_income").toStringArray();
        double[] incomeDouble = new double[incomeStr.length];

        for (int i = 0; i < incomeStr.length; i++) {
            String s = incomeStr[i]
                    .replace("$", "")   // $ ni olib tashlash
                    .replace("\"", "")  // " ni olib tashlash
                    .replace(",", "");  // vergulni olib tashlash
            incomeDouble[i] = Double.parseDouble(s);
        }
        df = df.drop("annual_income");

        return df.merge(DoubleVector.of("annual_income_clean", incomeDouble));
    }

    public DataFrame employmentLength(DataFrame df){
        String[] employmentLength = df.vector("employment_length").toStringArray();
        double[] employmentLengthDouble = new double[employmentLength.length];

        double sum = 0;
        int count = 0;

        for (int i=0; i<employmentLength.length; i++) {
            String v = employmentLength[i];
            if (StringUtils.hasText(v) && !v.equals("null")) {
                double d = Double.parseDouble(v.replace(",", "."));
                employmentLengthDouble[i] = d;
                sum += d;
                count++;
            }
        }

        double mean = sum / count;
        System.out.println("Employment Length Mean = " + mean);

        for (int i = 0; i < employmentLengthDouble.length; i++) {
            if (!StringUtils.hasText(employmentLength[i]) || employmentLength[i].equals("null")) {
                employmentLengthDouble[i] = mean;
            }
        }

        df = df.drop("employment_length");

        return df.merge(DoubleVector.of("employment_length_clean", employmentLengthDouble));
    }

    public DataFrame employmentType(DataFrame df){

        // employment_type normalizatsiya qilish
        String[] empType = df.stringVector("employment_type").toStringArray();
        for (int i = 0; i < empType.length; i++) {
            if (empType[i] == null || empType[i].isEmpty()) empType[i] = "Unknown";
            else {
                empType[i] = empType[i].toLowerCase().replace(" ", "_")
                        .replace("-", "_")
                        .replaceAll("contract$", "contractor")
                        .replace("pt", "part_time")
                        .replaceAll("self_emp$", "self_employed")
                        .replace("fulltime", "full_time")
                        .replace("part-time", "part_time")
                        .replace("ft", "full_time");
            }
        }
        df = df.drop("employment_type");

        return df.merge(StringVector.of("employment_type_c", empType));
    }

    public DataFrame education(DataFrame df){

        // education bo‘sh qiymat → "Unknown"
        String[] education = df.stringVector("education").toStringArray();
        for (int i = 0; i < education.length; i++) {
            if (education[i] == null || education[i].isEmpty()) education[i] = "Unknown";
        }
        return df.merge(StringVector.of("education_c", education));
    }

    public DataFrame maritalStatus(DataFrame df){

        // marital_status bo‘sh qiymat → "Unknown"
        String[] marital = df.stringVector("marital_status").toStringArray();
        for (int i = 0; i < marital.length; i++) {
            if (marital[i] == null || marital[i].isEmpty()) marital[i] = "Unknown";
        }
        return df.merge(StringVector.of("marital_status_c", marital));

    }

    public void insertDb(DataFrame df){
        int[] custNum = df.intVector("cust_id").toIntArray();
        int[] age = df.intVector("age").toIntArray();
        double[] annualIncode = df.doubleVector("annual_income_clean").toDoubleArray();
        double[] employmentLength = df.doubleVector("employment_length_clean").toDoubleArray();
        String[] employmentType = df.stringVector("employment_type_c").toStringArray();
        String[] education = df.stringVector("education_c").toStringArray();
        String[] maritalStatus = df.stringVector("marital_status_c").toStringArray();
        int[] numDependents = df.intVector("num_dependents").toIntArray();

        List<Demography> batch = new ArrayList<>(custNum.length);
        for (int i = 0; i < custNum.length; i++) {
            batch.add(new Demography((long) custNum[i], age[i], annualIncode[i], employmentLength[i], employmentType[i], education[i], maritalStatus[i], numDependents[i]));
        }

        demographyRepo.saveAll(batch);
    }
}
