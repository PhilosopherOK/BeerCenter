package work.project.beercenter.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "actions_id")
    private Long actionsId;

    @Column(name = "name_of_actions")
    private String nameOfActions;

    @Column(name = "short_descriptions")
    private String shortDescriptions;

    @Column(name = "path_of_photo")
    private String pathOfPhoto;

    public Action(String nameOfActions, String shortDescriptions, String pathOfPhoto) {
        this.nameOfActions = nameOfActions;
        this.shortDescriptions = shortDescriptions;
        this.pathOfPhoto = pathOfPhoto;
    }

    @Override
    public String toString() {
        return   nameOfActions + '\n' + shortDescriptions;
    }
}
