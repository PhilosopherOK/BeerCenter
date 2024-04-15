package work.project.beercenter.bot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import work.project.beercenter.model.Clients;
import work.project.beercenter.utils.Tools;

import java.io.InputStream;


@Getter
@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {


    private final Tools tools;

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    public ChatBot(Tools tools) {
        this.tools = tools;
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

        Clients clients = tools.getClientsService().findByChatId(chatId);

//        if (checkIfAdminCommand(user, text))
//            return;

        BotContext context;
        BotState state;

        // H -> Ph -> Em -> Th
        // 1 -> 2! -> 3! -> 4

        if (clients == null) {
            state = BotState.getInitialState();

            clients = new Clients(chatId, state.ordinal());
            tools.getClientsService().addUser(clients);

            context = BotContext.of(tools, this, clients, text);
            state.enter(context);

//            LOGGER.info("New user registered: " + chatId);
        } else {
            context = BotContext.of(tools, this, clients, text);
            state = BotState.byId(clients.getStateId());

//            LOGGER.info("Update received for user in state: " + state);
        }

        state.handleInput(context);

        // 1 -> 2 -> 3!
        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());

        clients.setStateId(state.ordinal());
        tools.getClientsService().updateUser(clients);
    }

    public void sendMessage(Clients clients, String text) {
        String chatId = String.valueOf(clients.getChatId());
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(Clients clients, String pathOfPhoto) {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream(pathOfPhoto);

        SendPhoto message = new SendPhoto();
        message.setChatId(Long.toString(clients.getChatId()));
        message.setPhoto(new InputFile(is, pathOfPhoto));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
