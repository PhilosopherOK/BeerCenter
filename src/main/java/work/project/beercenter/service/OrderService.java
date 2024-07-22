package work.project.beercenter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Client;
import work.project.beercenter.model.Orders;
import work.project.beercenter.model.Product;
import work.project.beercenter.repo.OrderRepo;
import work.project.beercenter.repo.ProductRepo;

import java.util.*;


@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepo ordersRepository;
    private final ProductRepo productRepository;

    @Transactional
    public List<Orders> findAllByClient(Client client) {
        return ordersRepository.findAllByClient(client);
    }


    @Transactional
    public void save(Orders orders) {
        if (orders.getItems() == null || orders.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order items list cannot be null or empty");
        }

        ordersRepository.save(orders);
    }

    @Transactional
    public void deleteById(long id) {
        ordersRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Orders findById(long id) {
        return ordersRepository.findById(id).orElse(null);
    }
}
