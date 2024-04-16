package work.project.beercenter.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "name_of_position")
    private String nameOfPosition;

    private Integer price;

    private Boolean active;

    @Column(name = "date_of_added")
    private Date dateOfAdded;

    public Menu(String nameOfPosition, Integer price, Boolean active, Date dateOfAdded) {
        this.nameOfPosition = nameOfPosition;
        this.price = price;
        this.active = active;
        this.dateOfAdded = dateOfAdded;
    }
}
