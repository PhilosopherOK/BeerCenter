package work.project.beercenter.model;

import javax.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
public class LoyaltyCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "loyalty_card_id")
    private Long loyaltyCardId;

    @Column(name = "bonus_balance")
    private Integer bonusBalance;

    @Column(name = "number_of_card")
    private Long numberOfCard;

    @Column(name = "path_of_barcode")
    private String pathOfBarcode;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;

    public LoyaltyCard(Integer bonusBalance, Long numberOfCard, String pathOfBarcode, Client client) {
        this.bonusBalance = bonusBalance;
        this.numberOfCard = numberOfCard;
        this.pathOfBarcode = pathOfBarcode;
        this.client = client;
    }
}
