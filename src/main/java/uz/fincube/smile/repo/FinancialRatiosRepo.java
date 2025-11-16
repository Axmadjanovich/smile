package uz.fincube.smile.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.fincube.smile.entity.FinancialRatiosEntity;

@Repository
public interface FinancialRatiosRepo extends JpaRepository<FinancialRatiosEntity, Integer> {
}
