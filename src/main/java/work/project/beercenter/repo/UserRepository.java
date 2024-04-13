package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import work.project.beercenter.model.Client;

import java.util.List;

public interface UserRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.notified = false " +
            "AND c.phone IS NOT NULL AND c.email IS NOT NULL")
    List<Client> findNewUsers();

    Client findByChatId(long id);
}
