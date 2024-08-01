package work.project.beercenter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "pices_id")
    private Integer pice;

    @Column(name = "products_id")
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "date_time_to_ready")
    private LocalDateTime dateTimeToReady;

    public Orders(Client client, List<OrderItem> items, Integer productId,Integer pice ,LocalDateTime dateTimeToReady) {
        this.client = client;
        this.items = items;
        this.dateTimeToReady = dateTimeToReady;
        this.pice = pice ;
        this.productId = productId;
    }
}
