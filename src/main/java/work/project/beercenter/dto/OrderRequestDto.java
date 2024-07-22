package work.project.beercenter.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
public class OrderRequestDto {
    private long chatId;
    private List<HashMap<Integer, Integer>> productsId;
    private LocalDateTime localDateTime;
}
