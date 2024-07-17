package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Location;

import java.util.List;

public interface LocationRepo extends JpaRepository<Location, Long> {
}
