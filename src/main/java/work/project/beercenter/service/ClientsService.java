package work.project.beercenter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Client;
import work.project.beercenter.repo.ClientsRepository;


import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;

    @Transactional(readOnly = true)
    public Client findByChatId(long id) {
        return clientsRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll(Pageable pageable) {
        return clientsRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Long count() {
        return clientsRepository.count();
    }

    @Transactional
    public void addUser(Client client) {
        client.setAdmin(clientsRepository.count() == 0);
        clientsRepository.save(client);
    }

    @Transactional
    public void updateUser(Client client) {
        clientsRepository.save(client);
    }
}

