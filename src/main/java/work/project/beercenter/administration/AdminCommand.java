package work.project.beercenter.administration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import work.project.beercenter.bot.ChatBot;
import work.project.beercenter.model.Clients;
import work.project.beercenter.utils.Tools;

import java.util.List;
@Data
@Component
public class AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class); //log4j
    private static final Integer SEND_MESSAGES_PER_ITERATION = 1;
    private static Integer PAGE_SEQUENCE_FOR_USERS_LIST = 0;
    private static final String BROADCAST = "broadcast";
    private static final String LIST_USERS = "users";
//    private final Tools tools;
//
//    public AdminCommand(Tools tools) {
//        this.tools = tools;
//    }
//
//    public boolean checkIfAdminCommand(Clients clients, String text) {
//        if (clients == null || !clients.getAdmin())
//            return false;
//
//        if (text.startsWith(BROADCAST)) {
//            LOGGER.info("Admin command received: " + BROADCAST);
//
//            text = text.substring(BROADCAST.length());
//            broadcast(text);
//
//            return true;
//        } else if (text.equals(LIST_USERS)) {
//            LOGGER.info("Admin command received: " + LIST_USERS);
//
//            listUsers(clients);
//            return true;
//        }
//
//        return false;
//    }
//    private void listUsers(Clients admin) {
//        StringBuilder sb = new StringBuilder("All clients list:\r\n");
//        if((tools.getClientsService().countUsers() / SEND_MESSAGES_PER_ITERATION) <= PAGE_SEQUENCE_FOR_USERS_LIST)
//            PAGE_SEQUENCE_FOR_USERS_LIST = 0;
//
//        List<Clients> clients = tools.getClientsService().findAllUsers(PageRequest.of(PAGE_SEQUENCE_FOR_USERS_LIST++,
//                SEND_MESSAGES_PER_ITERATION, Sort.Direction.DESC, "id"));
//
//        clients.forEach(client ->
//                sb.append(client.getClientId())
//                        .append(' ')
//                        .append(client.getPhone())
//                        .append(' ')
//                        .append(client.getEmail())
//                        .append("\r\n")
//        );
//
//        tools.getWorkingWithBot().sendPhoto(admin, "TODO"); //TODO
//        tools.getWorkingWithBot().sendMessage(admin, sb.toString());
//    }
//
//
//    private void broadcast(String text) {
//        for (int page = 0; page < tools.getClientsService().countUsers() / SEND_MESSAGES_PER_ITERATION; page++) {
//            List<Clients> clients = tools.getClientsService().findAllUsers(PageRequest.of(page,
//                    SEND_MESSAGES_PER_ITERATION, Sort.Direction.DESC, "id"));
//            clients.forEach(clients1 -> tools.getWorkingWithBot().sendMessage(clients1, text));
//        }
//    }
}
