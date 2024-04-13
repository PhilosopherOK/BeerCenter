package work.project.beercenter.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import work.project.beercenter.model.Client;
import work.project.beercenter.service.UserService;


import java.io.InputStream;
import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class); //log4j
    private static final Integer SEND_MESSAGES_PER_ITERATION = 1;
    private static Integer PAGE_SEQUENCE_FOR_USERS_LIST = 0;
    private static final String BROADCAST = "broadcast ";
    private static final String LIST_USERS = "users";
    private final UserService userService;
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    public ChatBot(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        final String text = update.getMessage().getText();
        final long chatId = update.getMessage().getChatId();

        Client client = userService.findByChatId(chatId);

        if (checkIfAdminCommand(client, text))
            return;

        BotContext context;
        BotState state;

        // H -> Ph -> Em -> Th
        // 1 -> 2! -> 3! -> 4

        if (client == null) {
            state = BotState.getInitialState();

            client = new Client(chatId, state.ordinal());
            userService.addUser(client);

            context = BotContext.of(this, client, text);
            state.enter(context);

            LOGGER.info("New client registered: " + chatId);
        } else {
            context = BotContext.of(this, client, text);
            state = BotState.byId(client.getStateId());

            LOGGER.info("Update received for client in state: " + state);
        }

        state.handleInput(context);

        // 1 -> 2 -> 3!
        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());

        client.setStateId(state.ordinal());
        userService.updateUser(client);
    }

    private boolean checkIfAdminCommand(Client client, String text) {
        if (client == null || !client.getAdmin())
            return false;

        if (text.startsWith(BROADCAST)) {
            LOGGER.info("Admin command received: " + BROADCAST);

            text = text.substring(BROADCAST.length());
            broadcast(text);

            return true;
        } else if (text.equals(LIST_USERS)) {
            LOGGER.info("Admin command received: " + LIST_USERS);

            listUsers(client);
            return true;
        }

        return false;
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(Long.toString(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(long chatId) {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("test.png");

        SendPhoto message = new SendPhoto();
        message.setChatId(Long.toString(chatId));
        message.setPhoto(new InputFile(is, "test"));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void listUsers(Client admin) {
        StringBuilder sb = new StringBuilder("All clients list:\r\n");
        if((userService.countUsers() / SEND_MESSAGES_PER_ITERATION) <= PAGE_SEQUENCE_FOR_USERS_LIST)
            PAGE_SEQUENCE_FOR_USERS_LIST = 0;

        List<Client> clients = userService.findAllUsers(PageRequest.of(PAGE_SEQUENCE_FOR_USERS_LIST++,
                SEND_MESSAGES_PER_ITERATION, Sort.Direction.DESC, "id"));

        clients.forEach(client ->
                sb.append(client.getClientId())
                        .append(' ')
                        .append(client.getPhone())
                        .append(' ')
                        .append(client.getEmail())
                        .append("\r\n")
        );

        sendPhoto(admin.getChatId());
        sendMessage(admin.getChatId(), sb.toString());
    }


    private void broadcast(String text) {
        for (int page = 0; page < userService.countUsers() / SEND_MESSAGES_PER_ITERATION; page++) {
            List<Client> clients = userService.findAllUsers(PageRequest.of(page,
                    SEND_MESSAGES_PER_ITERATION, Sort.Direction.DESC, "id"));
            clients.forEach(user -> sendMessage(user.getChatId(), text));
        }
    }
}
