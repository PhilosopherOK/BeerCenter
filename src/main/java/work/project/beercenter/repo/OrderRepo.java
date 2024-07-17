package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import work.project.beercenter.model.Client;
import work.project.beercenter.model.Orders;

import java.util.List;

public interface OrderRepo extends JpaRepository<Orders, Long> {
    List<Orders> findAllByClient(Client client);
}
