package work.project.beercenter.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequestDto {
    private long chatId;
    private List<Long> productsId;
    private LocalDateTime localDateTime;
}
