package work.project.beercenter.service;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Action;
import work.project.beercenter.repo.ActionsRepo;

import java.util.List;

@Data
@Service
public class ActionsService {
    private Integer ACTIONS_SEQUENCE = 0;
    private final Integer ACTIONS_PER_ITERATION = 1;
    private final ActionsRepo actionsRepo;

    @Transactional(readOnly = true)
    public void validationActionsSequence() {
        Integer howMuchActions = (int) actionsRepo.count();
        if ((howMuchActions / ACTIONS_PER_ITERATION) <= ACTIONS_SEQUENCE) {
            ACTIONS_SEQUENCE = 0;
        }
        if (ACTIONS_SEQUENCE < 0) {
            ACTIONS_SEQUENCE = (int) howMuchActions - 1;
        }
    }

    @Transactional(readOnly = true)
    public List<Action> getAction() {
        ACTIONS_SEQUENCE = 0;
        validationActionsSequence();
        return actionsRepo.findAll(PageRequest.of(ACTIONS_SEQUENCE,
                ACTIONS_PER_ITERATION, Sort.Direction.ASC, "actionsId")).getContent();
    }

    @Transactional(readOnly = true)
    public List<Action> getNextActions() {
        ++ACTIONS_SEQUENCE;
        validationActionsSequence();
        return actionsRepo.findAll(PageRequest.of(ACTIONS_SEQUENCE,
                ACTIONS_PER_ITERATION, Sort.Direction.ASC, "actionsId")).getContent();
    }

    @Transactional(readOnly = true)
    public List<Action> getPreviousActions() {
        if (ACTIONS_SEQUENCE > 0) {
            --ACTIONS_SEQUENCE;
        }
        validationActionsSequence();
        return actionsRepo.findAll(PageRequest.of(ACTIONS_SEQUENCE,
                ACTIONS_PER_ITERATION, Sort.Direction.ASC, "actionsId")).getContent();
    }

    @Transactional
    public void save(Action action) {
        actionsRepo.save(action);
    }

    @Transactional
    public void deleteById(Long actionId) {
        actionsRepo.deleteById(actionId);
    }

    public Action getActionFromPhotoPathAndDescription(String photoPath, String description) throws Exception {
        String[] strings = description.split("~");
        if (strings.length == 1) {
            throw new Exception("Некоректний ввiд !");
        }
        return new Action(strings[0], strings[1], photoPath);
    }

}
