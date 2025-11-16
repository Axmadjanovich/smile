package uz.fincube.smile.service;

import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Service;
import smile.classification.LogisticRegression;
import smile.data.DataFrame;
import smile.io.Read;
import smile.math.MathEx;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class Teaching {

    public void trainModel(String path) throws IOException, URISyntaxException {
        DataFrame df = Read.csv(path, CSVFormat.DEFAULT.withFirstRecordAsHeader());

        int[] y = df.intVector("default_flag").toIntArray();
        df = df.drop("default_flag");
        double[][] x = df.toArray(df.names());

        int[] idx = MathEx.permutate(y.length);
        int trainSize = (int)(y.length * 0.7);

        double[][] x_train = new double[trainSize][];
        int[] y_train = new int[trainSize];
        double[][] x_test = new double[y.length - trainSize][];
        int[] y_test = new int[y.length - trainSize];

        for (int i = 0; i < y.length; i++) {
            if (i < trainSize) {
                x_train[i] = x[idx[i]];
                y_train[i] = y[idx[i]];
            } else {
                x_test[i - trainSize] = x[idx[i]];
                y_test[i - trainSize] = y[idx[i]];
            }
        }

        LogisticRegression lr = LogisticRegression.fit(x_train, y_train);

        int[] y_pred = lr.predict(x_test);

        int correct = 0;
        for (int i = 0; i < y_test.length; i++) {
            if (y_test[i] == y_pred[i]) correct++;
        }
        double acc = (double) correct / y_test.length;
        System.out.println("Accuracy: " + acc);

    }
}
