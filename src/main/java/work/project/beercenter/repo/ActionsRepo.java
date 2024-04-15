package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Actions;

public interface ActionsRepo extends JpaRepository<Actions, Long> {
}
