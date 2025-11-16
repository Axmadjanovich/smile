package uz.fincube.smile.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.fincube.smile.model.LoanDetails;

@Repository
public interface LoanDetailRepo extends JpaRepository<LoanDetails, Integer> {
}
