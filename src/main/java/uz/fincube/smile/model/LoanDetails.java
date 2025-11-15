package uz.fincube.smile.model;

import lombok.Data;

@Data
public class LoanDetails {
    public int customerId;
    public String loanType;
    public double loanAmount;
    public int loanTerm;
    public double interestRate;
    public String loanPurpose;
    public double loanToValueRatio;
    public String originationChannel;
    public int loanOfficerId;
    public String marketingCampaign;
}
