package work.project.beercenter.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Actions;
import work.project.beercenter.repo.ActionsRepo;

import java.util.List;

@Service
public class ActionsService {
    private static Integer ACTIONS_SEQUENCE = 0;
    private static final Integer ACTIONS_PER_ITERATION = 1;
    private final ActionsRepo actionsRepo;

    public ActionsService(ActionsRepo actionsRepo) {
        this.actionsRepo = actionsRepo;
    }
    @Transactional(readOnly = true)
    public void validationActionsSequence(){
        Integer howMuchActions = (int) actionsRepo.count();
        if((howMuchActions / ACTIONS_PER_ITERATION) <= ACTIONS_SEQUENCE){
            ACTIONS_SEQUENCE = 0;
        }
        if(ACTIONS_SEQUENCE < 0){
            ACTIONS_SEQUENCE = (int) howMuchActions - 1;
        }
    }

    @Transactional(readOnly = true)
    public List<Actions> getActions(){
        ACTIONS_SEQUENCE = 0;
        validationActionsSequence();
        return actionsRepo.findAll(PageRequest.of(ACTIONS_SEQUENCE,
                ACTIONS_PER_ITERATION, Sort.Direction.ASC, "id")).getContent();
    }
    @Transactional(readOnly = true)
    public List<Actions> getNextActions(){
        ++ACTIONS_SEQUENCE;
        validationActionsSequence();
        return actionsRepo.findAll(PageRequest.of(ACTIONS_SEQUENCE,
                ACTIONS_PER_ITERATION, Sort.Direction.ASC, "id")).getContent();
    }

    @Transactional(readOnly = true)
    public List<Actions> getPreviousActions(){
        --ACTIONS_SEQUENCE;
        validationActionsSequence();
        return actionsRepo.findAll(PageRequest.of(ACTIONS_SEQUENCE,
                ACTIONS_PER_ITERATION, Sort.Direction.ASC, "id")).getContent();
    }
}
