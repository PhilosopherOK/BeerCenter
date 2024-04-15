package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Locations;

public interface LocationsRepo extends JpaRepository<Locations, Long> {
}
