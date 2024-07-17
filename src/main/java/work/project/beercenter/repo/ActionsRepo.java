package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Action;

public interface ActionsRepo extends JpaRepository<Action, Long> {
}
