package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Rules;

public interface RulesRepo extends JpaRepository<Rules, Long> {
}
