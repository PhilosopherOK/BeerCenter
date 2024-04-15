package work.project.beercenter.service;

import org.springframework.stereotype.Service;
import work.project.beercenter.repo.RulesRepo;

@Service
public class RulesService {
    private final RulesRepo rulesRepo;

    public RulesService(RulesRepo rulesRepo) {
        this.rulesRepo = rulesRepo;
    }


}
