package work.project.beercenter.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rules_id")
    private Long rulesId;

    private String name;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "starting_date")
    private Date startingDate;

    private String description;

    public Rule(String name, Date startingDate, String description) {
        this.name = name;
        this.startingDate = startingDate;
        this.description = description;
    }
}
