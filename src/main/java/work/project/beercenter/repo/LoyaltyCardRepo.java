package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.LoyaltyCard;

public interface LoyaltyCardRepo extends JpaRepository<LoyaltyCard, Long> {
}
