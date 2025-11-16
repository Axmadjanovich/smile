package uz.fincube.smile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class LoanDetails {
    @Id
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
