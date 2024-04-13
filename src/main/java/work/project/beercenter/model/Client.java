package work.project.beercenter.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    private String email;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "date_of_birthday")
    private Date dateOfBirthday;

    private String address;

    private Boolean admin;

    @OneToOne(mappedBy = "client")
    private LoyaltyCard loyaltyCard;

    private Boolean notified = false;

    public Client() {
        address = "даннi вiдсутнi";
    }

    public Client(Long chatId, Integer state) {
        address = "даннi вiдсутнi";
        this.chatId = chatId;
    }

    public Client(Long chatId, Integer stateId, Boolean admin) {
        address = "даннi вiдсутнi";
        this.chatId = chatId;
        this.admin = admin;
    }

}
