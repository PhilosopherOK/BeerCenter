package work.project.beercenter.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import work.project.beercenter.dto.OrderRequestDto;
import work.project.beercenter.model.Client;
import work.project.beercenter.model.Orders;
import work.project.beercenter.model.Product;
import work.project.beercenter.utils.Tools;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OrderController {

    private final Tools tools;

    @PostMapping("/addOrder")
    public void addItem(@RequestBody OrderRequestDto orderRequestDto) {
        Client client = tools.getClientsService().findByChatId(orderRequestDto.getChatId());
//        System.out.println("1");

        if (client == null) {
            return;
        } else {

            tools.getOrderService().save(new Orders(client, null,
                    orderRequestDto.getLocalDateTime()), orderRequestDto.getProductsId());
        }
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        System.out.println("get");

        return tools.getProductService().findAll();
    }
}
