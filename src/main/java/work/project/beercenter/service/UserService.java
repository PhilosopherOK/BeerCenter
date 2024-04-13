package work.project.beercenter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.project.beercenter.model.Client;
import work.project.beercenter.repo.UserRepository;


import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Client findByChatId(long id) {
        return userRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public List<Client> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }
    @Transactional(readOnly = true)
    public List<Client> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Long countUsers(){
        return userRepository.count();
    }


    @Transactional
    public List<Client> findNewUsers() {
        List<Client> clients = userRepository.findNewUsers();

        clients.forEach((user) -> user.setNotified(true));
        userRepository.saveAll(clients);

        return clients;
    }

    @Transactional
    public void addUser(Client client) {
        client.setAdmin(userRepository.count() == 0);
        userRepository.save(client);
    }

    @Transactional
    public void updateUser(Client client) {
        userRepository.save(client);
    }
}

