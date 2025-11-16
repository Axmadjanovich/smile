package uz.fincube.smile.service;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smile.data.DataFrame;
import smile.data.vector.DoubleVector;
import smile.data.vector.IntVector;
import smile.io.JSON;
import smile.io.Read;
import uz.fincube.smile.entity.FinancialRatiosEntity;
import uz.fincube.smile.repo.FinancialRatiosRepo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FinancialRatios {

    @Autowired
    private FinancialRatiosRepo financialRatiosRepo;

    public void read(String path) throws IOException, URISyntaxException {

        BufferedReader reader = new BufferedReader(new FileReader(path));
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature());

        List<Integer> custNum = new ArrayList<>();
        List<Double> monthlyIncome = new ArrayList<>();
        List<Double> existingDebt = new ArrayList<>();
        List<Double> monthlyPayment = new ArrayList<>();
        List<Double> debtToIncome = new ArrayList<>();
        List<Double> debtServiceRatio = new ArrayList<>();
        List<Double> paymentToIncomeRatio = new ArrayList<>();
        List<Double> creditUtilization = new ArrayList<>();
        List<Double> revolvingBalance = new ArrayList<>();
        List<Double> creditUsageAmount = new ArrayList<>();
        List<Double> availableCredit = new ArrayList<>();
        List<Double> totalMonthlyDebtPayment = new ArrayList<>();
        List<Double> annualDebtPayment = new ArrayList<>();
        List<Double> loanToAnnualIncome = new ArrayList<>();
        List<Double> totalDebtAmount = new ArrayList<>();
        List<Double> monthlyFreeCashFlow = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            Map<String, Object> obj = mapper.readValue(line, Map.class);

            custNum.add((Integer)obj.get("cust_num"));

            monthlyIncome.add(parseDollar((String)obj.get("monthly_income")));
            existingDebt.add(parseDollar((String)obj.get("existing_monthly_debt")));
            monthlyPayment.add(parseDollar((String)obj.get("monthly_payment")));

            debtToIncome.add(parseNumber(obj.get("debt_to_income_ratio")));
            debtServiceRatio.add(parseNumber(obj.get("debt_service_ratio")));
            paymentToIncomeRatio.add(parseNumber(obj.get("payment_to_income_ratio")));
            creditUtilization.add(parseNumber(obj.get("credit_utilization")));

            revolvingBalance.add(parseDollar(obj.get("revolving_balance").toString()));
            creditUsageAmount.add(parseDollar((String)obj.get("credit_usage_amount")));
            availableCredit.add(parseDollar((String)obj.get("available_credit")));
            totalMonthlyDebtPayment.add(parseDollar((String)obj.get("total_monthly_debt_payment")));
            annualDebtPayment.add(parseNumber(obj.get("annual_debt_payment")));
            loanToAnnualIncome.add(parseNumber(obj.get("loan_to_annual_income")));
            totalDebtAmount.add(parseDollar((String)obj.get("total_debt_amount")));
            monthlyFreeCashFlow.add(parseDollar((String)obj.get("monthly_free_cash_flow")));
        }

        DataFrame df = DataFrame.of(
                IntVector.of("cust_num", custNum.stream().mapToInt(Integer::intValue).toArray()),
                DoubleVector.of("monthly_income", monthlyIncome.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("existing_monthly_debt", existingDebt.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("monthly_payment", monthlyPayment.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("debt_to_income_ratio", debtToIncome.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("debt_service_ratio", debtServiceRatio.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("payment_to_income_ratio", paymentToIncomeRatio.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("credit_utilization", creditUtilization.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("revolving_balance", revolvingBalance.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("credit_usage_amount", creditUsageAmount.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("available_credit", availableCredit.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("total_monthly_debt_payment", totalMonthlyDebtPayment.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("annual_debt_payment", annualDebtPayment.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("loan_to_annual_income", loanToAnnualIncome.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("total_debt_amount", totalDebtAmount.stream().mapToDouble(Double::doubleValue).toArray()),
                DoubleVector.of("monthly_free_cash_flow", monthlyFreeCashFlow.stream().mapToDouble(Double::doubleValue).toArray())
        );

        List<FinancialRatiosEntity> list = new ArrayList<>();

        for (int i = 0; i < custNum.size(); i++) {
            FinancialRatiosEntity fr = FinancialRatiosEntity.builder()
                    .custNum(custNum.get(i))
                    .monthlyIncome(monthlyIncome.get(i))
                    .existingDebt(existingDebt.get(i))
                    .monthlyPayment(monthlyPayment.get(i))
                    .debtToIncome(debtToIncome.get(i))
                    .debtServiceRatio(debtServiceRatio.get(i))
                    .paymentToIncomeRatio(paymentToIncomeRatio.get(i))
                    .creditUtilization(creditUtilization.get(i))
                    .revolvingBalance(revolvingBalance.get(i))
                    .creditUsageAmount(creditUsageAmount.get(i))
                    .availableCredit(availableCredit.get(i))
                    .totalMonthlyDebtPayment(totalMonthlyDebtPayment.get(i))
                    .annualDebtPayment(annualDebtPayment.get(i))
                    .loanToAnnualIncome(loanToAnnualIncome.get(i))
                    .totalDebtAmount(totalDebtAmount.get(i))
                    .monthlyFreeCashFlow(monthlyFreeCashFlow.get(i))
                    .build();

            list.add(fr);
        }

        financialRatiosRepo.saveAll(list);

        System.out.println("\n\n\n\n\n========================================FINANCIAL RATIOS=======================================\n\n\n\n");

        System.out.println(df.structure());
        System.out.println(df.summary());
    }


    // Helper function: $ va , olib tashlash
    private static double parseDollar(String s) {
        if (s == null || s.equals("NaN")) return 0.0;
        s = s.replace("$", "").replace(",", "").replace("\"", "");
        return Double.parseDouble(s);
    }

    // Helper function: null yoki NaN
    private static double parseNumber(Object o) {
        if (o == null) return 0.0;
        if (o.toString().equals("NaN")) return 0.0;
        return ((Number)o).doubleValue();
    }
}
