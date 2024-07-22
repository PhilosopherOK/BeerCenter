package work.project.beercenter.model;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name="product_generator", sequenceName = "product_seq", allocationSize=1)
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
