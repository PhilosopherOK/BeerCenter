package work.project.beercenter.service;

import org.springframework.stereotype.Service;
import work.project.beercenter.model.LoyaltyCard;
import work.project.beercenter.repo.LoyaltyCardRepo;

@Service
public class LoyaltyCardService {
    private final LoyaltyCardRepo loyaltyCardRepo;

    public LoyaltyCardService(LoyaltyCardRepo loyaltyCardRepo) {
        this.loyaltyCardRepo = loyaltyCardRepo;
    }

    public Long count() {
        return loyaltyCardRepo.count();
    }
    public void save(LoyaltyCard loyaltyCard){
        loyaltyCardRepo.save(loyaltyCard);
    }
}
