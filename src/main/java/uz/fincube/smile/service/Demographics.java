package uz.fincube.smile.service;

import ch.qos.logback.core.util.StringUtil;
import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import smile.data.DataFrame;
import smile.data.vector.DoubleVector;
import smile.data.vector.StringVector;
import smile.io.Read;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class Demographics {

    public void read(String path) throws IOException, URISyntaxException {

        DataFrame df = Read.csv(path, CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(','));

        String[] incomeStr = df.stringVector("annual_income").toStringArray();
        double[] incomeDouble = new double[incomeStr.length];

        for (int i = 0; i < incomeStr.length; i++) {
            String s = incomeStr[i]
                    .replace("$", "")   // $ ni olib tashlash
                    .replace("\"", "")  // " ni olib tashlash
                    .replace(",", "");  // vergulni olib tashlash
            incomeDouble[i] = Double.parseDouble(s);
        }

        String[] employmentLength = df.vector("employment_length").toStringArray();
        double[] employmentLengthDouble = new double[employmentLength.length];

        for (int i=0; i<employmentLengthDouble.length; i++){
            String s = employmentLength[i];
            double d = 0;
            if (StringUtils.hasText(s)) d = Double.parseDouble(s.replace(",", ".").replace("null", "0"));
            employmentLengthDouble[i] = d;
        }

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
        df = df.merge(StringVector.of("employment_type_c", empType));


        // education bo‘sh qiymat → "Unknown"
        String[] education = df.stringVector("education").toStringArray();
        for (int i = 0; i < education.length; i++) {
            if (education[i] == null || education[i].isEmpty()) education[i] = "Unknown";
        }
        df = df.merge(StringVector.of("education_c", education));

        // marital_status bo‘sh qiymat → "Unknown"
        String[] marital = df.stringVector("marital_status").toStringArray();
        for (int i = 0; i < marital.length; i++) {
            if (marital[i] == null || marital[i].isEmpty()) marital[i] = "Unknown";
        }
        df = df.merge(StringVector.of("marital_status_c", marital));


        df = df.merge(DoubleVector.of("employment_length_clean", employmentLengthDouble));
        df = df.merge(DoubleVector.of("annual_income_clean", incomeDouble));

        // eski string ustunni o‘chirish
        df = df.drop("annual_income");
        df = df.drop("employment_length");
        df = df.drop("employment_type");

        System.out.println("\n\n\n\n\n================================================DEMOGRAPHICS================================================\n\n\n\n\n");

        System.out.println(df.structure());
        System.out.println(df.summary());

        System.out.println("employment_type_c : " + Arrays.stream(df.stringVector("employment_type_c").toStringArray())
                .collect(Collectors.toSet()));

        System.out.println("education : " + Arrays.stream(df.stringVector("education_c").toStringArray())
                .collect(Collectors.toSet()));

        System.out.println("marital_status_c : " + Arrays.stream(df.stringVector("marital_status_c").toStringArray())
                .collect(Collectors.toSet()));
    }
}
