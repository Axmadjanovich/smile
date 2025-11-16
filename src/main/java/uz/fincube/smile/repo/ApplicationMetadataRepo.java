package uz.fincube.smile.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.fincube.smile.model.ApplicationMetadata;

@Repository
public interface ApplicationMetadataRepo extends JpaRepository<ApplicationMetadata, Integer> {
}
