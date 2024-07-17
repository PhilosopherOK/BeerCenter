package work.project.beercenter.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import work.project.beercenter.model.LoyaltyCard;
import work.project.beercenter.repo.LoyaltyCardRepo;

@Data
@Service
public class LoyaltyCardService {
    private final LoyaltyCardRepo loyaltyCardRepo;

    public Long count() {
        return loyaltyCardRepo.count();
    }
    public void save(LoyaltyCard loyaltyCard){
        loyaltyCardRepo.save(loyaltyCard);
    }
}
