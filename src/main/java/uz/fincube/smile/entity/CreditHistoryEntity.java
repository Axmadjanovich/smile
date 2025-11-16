package uz.fincube.smile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "creadit_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditHistoryEntity {

    @Id
    @Column(name = "customer_number")
    private Long customerNumber;

    @Column(name = "credit_score")
    private Long creditScore;

    @Column(name = "num_credit_accounts")
    private Long numCreditAccounts;

    @Column(name = "oldest_credit_line_age")
    private Double oldestCreditLineAge;

    @Column(name = "oldest_account_age_months")
    private Double oldestAccountAgeMonths;

    @Column(name = "total_credit_limit")
    private Double totalCreditLimit;

    @Column(name = "num_delinquencies_2yrs")
    private Double numDelinquencies2yrs;

    @Column(name = "num_inquiries_6mo")
    private Long numInquiries6mo;

    @Column(name = "recent_inquiry_count")
    private Long recentInquiryCount;

    @Column(name = "num_public_records")
    private Long numPublicRecords;

    @Column(name = "num_collections")
    private Long numCollections;

    @Column(name = "account_diversity_index")
    private Double accountDiversityIndex;
}
