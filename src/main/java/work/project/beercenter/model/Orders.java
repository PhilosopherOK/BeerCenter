package work.project.beercenter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<Product> products;

    @Column(name = "date_time_to_ready")
    private LocalDateTime dateTimeToReady;

    public Orders(Client client, List<Product> products, LocalDateTime dateTimeToReady) {
        this.client = client;
        this.products = products;
        this.dateTimeToReady = dateTimeToReady;
    }
}
