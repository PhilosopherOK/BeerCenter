package work.project.beercenter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Clients;
import work.project.beercenter.repo.ClientsRepository;


import java.util.List;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;

    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    @Transactional(readOnly = true)
    public Clients findByChatId(long id) {
        return clientsRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public List<Clients> findAllUsers(Pageable pageable) {
        return clientsRepository.findAll(pageable).getContent();
    }
    @Transactional(readOnly = true)
    public List<Clients> findAllUsers() {
        return clientsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Long countUsers(){
        return clientsRepository.count();
    }


    @Transactional
    public void addUser(Clients clients) {
        clients.setAdmin(clientsRepository.count() == 0);
        clientsRepository.save(clients);
    }

    @Transactional
    public void updateUser(Clients clients) {
        clientsRepository.save(clients);
    }
}

