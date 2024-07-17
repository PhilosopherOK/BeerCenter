package work.project.beercenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import work.project.beercenter.model.Product;


public interface ProductRepo extends JpaRepository<Product, Long> {

    @Modifying
    @Query("DELETE FROM Product p WHERE p.name = :name AND p.price = :price")
    int deleteByNameAndPrice(@Param("name") String name, @Param("price") Integer price);
}
