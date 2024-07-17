package work.project.beercenter.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import work.project.beercenter.model.Location;
import work.project.beercenter.repo.LocationRepo;

import java.util.List;

@Data
@Service
public class LocationsService {
    private final LocationRepo locationRepo;

    public List<Location> findAll() {
        return locationRepo.findAll();
    }

    public void save(Location location) {
        locationRepo.save(location);
    }

    public void deleteById(Long id) {
        locationRepo.deleteById(id);
    }
}
