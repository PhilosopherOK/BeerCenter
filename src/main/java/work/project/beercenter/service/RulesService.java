package work.project.beercenter.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Rule;
import work.project.beercenter.repo.RuleRepo;

import java.util.List;


@Data
@Service
public class RulesService {
    private final RuleRepo ruleRepo;

    @Transactional(readOnly = true)
    public List<Rule> findAll() {
        return ruleRepo.findAll();
    }

    @Transactional
    public void save(Rule rule) {
        ruleRepo.save(rule);
    }

    @Transactional
    public void deleteByName(String name) {
            ruleRepo.deleteRuleByName(name);
    }

    @Transactional
    public void deleteById(Long actionId) {
        ruleRepo.deleteById(actionId);
    }


}
