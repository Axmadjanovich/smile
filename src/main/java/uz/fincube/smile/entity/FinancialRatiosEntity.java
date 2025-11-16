package uz.fincube.smile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "financial_ratios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRatiosEntity {

    @Id
    @Column(name = "cust_num")
    private Integer custNum;

    @Column(name = "monthly_income")
    private Double monthlyIncome;

    @Column(name = "existing_monthly_debt")
    private Double existingDebt;

    @Column(name = "monthly_payment")
    private Double monthlyPayment;

    @Column(name = "debt_to_income_ratio")
    private Double debtToIncome;

    @Column(name = "debt_service_ratio")
    private Double debtServiceRatio;

    @Column(name = "payment_to_income_ratio")
    private Double paymentToIncomeRatio;

    @Column(name = "credit_utilization")
    private Double creditUtilization;

    @Column(name = "revolving_balance")
    private Double revolvingBalance;

    @Column(name = "credit_usage_amount")
    private Double creditUsageAmount;

    @Column(name = "available_credit")
    private Double availableCredit;

    @Column(name = "total_monthly_debt_payment")
    private Double totalMonthlyDebtPayment;

    @Column(name = "annual_debt_payment")
    private Double annualDebtPayment;

    @Column(name = "loan_to_annual_income")
    private Double loanToAnnualIncome;

    @Column(name = "total_debt_amount")
    private Double totalDebtAmount;

    @Column(name = "monthly_free_cash_flow")
    private Double monthlyFreeCashFlow;
}
