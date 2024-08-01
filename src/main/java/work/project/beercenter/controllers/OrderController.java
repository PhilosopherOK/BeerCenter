package work.project.beercenter.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import work.project.beercenter.bot.BotState;
import work.project.beercenter.dto.OrderRequestDto;
import work.project.beercenter.model.Client;
import work.project.beercenter.model.OrderItem;
import work.project.beercenter.model.Orders;
import work.project.beercenter.model.Product;
import work.project.beercenter.service.ProductService;
import work.project.beercenter.utils.Tools;
import work.project.beercenter.utils.keyboards.Buttons;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final Tools tools;
    private final ProductService productService;

    @PostMapping("/addOrder")
    public void addItem(@RequestBody OrderRequestDto orderRequestDto) {
        logger.info("Received order request: {}", orderRequestDto);

        Client client = tools.getClientsService().findByChatId(orderRequestDto.getChatId());

        if (client == null) {
            logger.warn("Client not found for chatId: {}", orderRequestDto.getChatId());
            return ;
        }

        Integer pice = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (HashMap<Integer, Integer> hashMap : orderRequestDto.getProductsId()) {
            for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
                Integer productId = entry.getKey();
                pice = entry.getValue();
                Product product = productService.findById(Long.valueOf(productId));

                orderItems.add(new OrderItem(product, pice));

                Orders order = new Orders(client, orderItems, productId,pice, orderRequestDto.getLocalDateTime());
                tools.getOrderService().save(order);
            }
        }
    }


    @GetMapping("/products")
    public List<Product> getProducts() {
        logger.info("Fetching products");
        return tools.getProductService().findAll();
    }

}
