package uz.fincube.smile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import smile.data.DataFrame;
import smile.io.Read;
import uz.fincube.smile.entity.CreditHistoryEntity;
import uz.fincube.smile.repo.CreditHistoryRepo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditHistory {

    @Autowired
    private CreditHistoryRepo creditHistoryRepo;

    public void read(String path) throws IOException, URISyntaxException {
        DataFrame df = Read.parquet(path);

        System.out.println("======================================================CREDIT HISTORY======================================================");

        creditHistoryRepo.saveAll(map(df));
    }

    public List<CreditHistoryEntity> map(DataFrame df){
        List<CreditHistoryEntity> list = new ArrayList<>();

        for (int i = 0; i < df.size(); i++) {
            CreditHistoryEntity c = CreditHistoryEntity.builder()
                    .customerNumber(df.getLong(i, "customer_number"))
                    .creditScore(df.getLong(i, "credit_score"))
                    .numCreditAccounts(df.getLong(i, "num_credit_accounts"))
                    .oldestCreditLineAge(df.getDouble(i, "oldest_credit_line_age"))
                    .oldestAccountAgeMonths(df.getDouble(i, "oldest_account_age_months"))
                    .totalCreditLimit(df.getDouble(i, "total_credit_limit"))
                    .numDelinquencies2yrs(StringUtils.hasText(df.getString(i, "num_delinquencies_2yrs")) ? Double.parseDouble(df.getString(i, "num_delinquencies_2yrs")) : null)
                    .numInquiries6mo(df.getLong(i, "num_inquiries_6mo"))
                    .recentInquiryCount(df.getLong(i, "recent_inquiry_count"))
                    .numPublicRecords(df.getLong(i, "num_public_records"))
                    .numCollections(df.getLong(i, "num_collections"))
                    .accountDiversityIndex(df.getDouble(i, "account_diversity_index"))
                    .build();

            list.add(c);
        }

        return list;
    }
}
