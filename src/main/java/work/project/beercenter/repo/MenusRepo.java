package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Menu;

public interface MenusRepo extends JpaRepository<Menu, Long> {
}
