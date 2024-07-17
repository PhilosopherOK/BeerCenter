package work.project.beercenter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Product;
import work.project.beercenter.repo.ProductRepo;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo productRepo;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Transactional
    public void save(Product product) {
        productRepo.save(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepo.deleteById(id);
    }

    @Transactional
    public boolean deleteByNameAndPrice(String name, Integer price) {
        try{
            return productRepo.deleteByNameAndPrice(name, price) > 0;
        }catch (Exception e){
            return false;
        }
    }

}
