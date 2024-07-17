package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Client;

public interface ClientsRepository extends JpaRepository<Client, Long> {
    Client findByChatId(long id);
}
