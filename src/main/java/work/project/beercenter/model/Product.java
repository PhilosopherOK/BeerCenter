package work.project.beercenter.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name_of_position")
    private String name;

    private Integer price;
    private String pathOfPhoto;

    @Column(name = "date_of_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfAdded;

    public Product(String name, Integer price, String pathOfPhoto, Date dateOfAdded) {
        this.name = name;
        this.price = price;
        this.pathOfPhoto = pathOfPhoto;
        this.dateOfAdded = dateOfAdded;
    }

    @Override
    public String toString() {
        return "name: " + name + "\n"
                + "price: " + price + "\n";
    }
}
