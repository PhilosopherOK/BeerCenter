package work.project.beercenter.service;

import org.springframework.stereotype.Service;
import work.project.beercenter.repo.MenusRepo;

@Service
public class MenusService {
    private final MenusRepo menusRepo;

    public MenusService(MenusRepo menusRepo) {
        this.menusRepo = menusRepo;
    }


}
