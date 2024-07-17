package work.project.beercenter.model;

import javax.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders;

    @OneToOne(mappedBy = "client")
    private LoyaltyCard loyaltyCard;

    private Integer verificationCode;

    public Client() {
        address = "даннi вiдсутнi";
        admin = false;
    }

    public Client(Long chatId, Integer state) {
        address = "даннi вiдсутнi";
        this.chatId = chatId;
        admin = false;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", chatId=" + chatId +
                ", stateId=" + stateId +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirthday=" + dateOfBirthday +
                ", address='" + address + '\'' +
                ", admin=" + admin +
                ", verificationCode=" + verificationCode +
                '}';
    }
}
