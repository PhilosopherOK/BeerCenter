package work.project.beercenter.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Locations {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "locations_id")
    private Long locationsId;

    @Column(name = "store_address")
    private String storeAddress;

    private String schedule;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "opening_date")
    private Date openingDate;

    private Boolean active;

    public Locations(String storeAddress, String schedule, Date openingDate, Boolean active) {
        this.storeAddress = storeAddress;
        this.schedule = schedule;
        this.openingDate = openingDate;
        this.active = active;
    }
}
