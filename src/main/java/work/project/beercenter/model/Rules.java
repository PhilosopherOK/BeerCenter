package work.project.beercenter.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Rules {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rules_id")
    private Long rulesId;

    private String  nameOfRule;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "starting_date")
    private Date startingDate;

    private String description;

    public Rules(String nameOfRule, Date startingDate, String description) {
        this.nameOfRule = nameOfRule;
        this.startingDate = startingDate;
        this.description = description;
    }
}
