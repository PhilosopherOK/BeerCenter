package work.project.beercenter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Client;
import work.project.beercenter.model.Orders;
import work.project.beercenter.model.Product;
import work.project.beercenter.repo.OrderRepo;
import work.project.beercenter.repo.ProductRepo;

import java.util.List;


@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    @Transactional
    public List<Orders> findAllByClient(Client client) {
        return orderRepo.findAllByClient(client);
    }

    @Transactional
    public void save(Orders orders, List<Long> productsId) {
        List<Product> productList = productsId.stream().map(id -> productRepo.findById(id).get()).toList();
        orders.setProducts(productList);
        orderRepo.save(orders);
    }

    @Transactional
    public void deleteById(long id) {
        orderRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Orders findById(long id) {
        return orderRepo.findById(id).orElse(null);
    }
}
