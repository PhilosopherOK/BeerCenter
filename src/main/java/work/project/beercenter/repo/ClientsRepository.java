package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import work.project.beercenter.model.Clients;

import java.util.List;

public interface ClientsRepository extends JpaRepository<Clients, Long> {
    Clients findByChatId(long id);
}
