package work.project.beercenter.service;

import org.springframework.stereotype.Service;
import work.project.beercenter.repo.LocationsRepo;

@Service
public class LocationsService {
    private final LocationsRepo locationsRepo;

    public LocationsService(LocationsRepo locationsRepo) {
        this.locationsRepo = locationsRepo;
    }


}
