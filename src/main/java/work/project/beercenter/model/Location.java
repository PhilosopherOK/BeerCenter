package work.project.beercenter.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "locations_id")
    private Long locationsId;

    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "link_to_google_maps")
    private String linkToGoogleMaps;

    private String schedule;

//    @Temporal(value = TemporalType.DATE)
//    @Column(name = "opening_date")
//    private Date openingDate;
//
//    private Boolean active;

    public Location(String storeAddress, String schedule) {
        this.storeAddress = storeAddress;
        this.schedule = schedule;
    }
}
