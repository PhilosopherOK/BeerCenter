package work.project.beercenter.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import work.project.beercenter.model.Product;
import work.project.beercenter.repo.OrderRepo;
import work.project.beercenter.service.ProductService;

import java.util.Date;

@Configuration
public class AppConfig {


    @Bean
    public CommandLineRunner demo(OrderRepo orderRepo) {
        return args -> {
            orderRepo.deleteAll();

//            productService.save(new Product("firstProd", 25,
//                    "https://img.freepik.com/free-photo/view-3d-adorable-cat-with-fluffy-clouds_23-2151113419.jpg?t=st=1721143620~exp=1721147220~hmac=7cdf389acb92df85e83e26413c6d5b508d082dddaa31119d8c098c62817f1838&w=360"
//                    , new Date()));
//            productService.save(new Product("secondProd", 35,
//                    "https://img.freepik.com/premium-photo/ai-generated-ai-generative-cat-sitting-close-to-neon-glowing-light-portrait-pet-animal-face_95211-30503.jpg?w=1380"
//                    , new Date()));
        };
    }
}


