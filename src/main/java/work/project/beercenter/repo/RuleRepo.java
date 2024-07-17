package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Rule;

public interface RuleRepo extends JpaRepository<Rule, Long> {
    void deleteRuleByName(String name);
}
