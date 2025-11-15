package uz.fincube.smile.service;

import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Service;
import smile.data.DataFrame;
import smile.data.vector.DoubleVector;
import smile.io.Read;

import java.io.IOException;
import java.net.URISyntaxException;

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

        df = df.merge(DoubleVector.of("annual_income_clean", incomeDouble));

        // eski string ustunni o‘chirish
        df = df.drop("annual_income");


        System.out.println(df.summary());

    }
}
