package work.project.beercenter.bot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import work.project.beercenter.model.Client;
import work.project.beercenter.utils.Tools;
import work.project.beercenter.utils.keyboards.StandardKeyboard;

import java.io.*;
import java.util.List;


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
        if (!update.hasMessage())
            return;
        final long chatId = update.getMessage().getChatId();
        Client client = tools.getClientsService().findByChatId(chatId);
        if (!update.getMessage().hasText()) {
            if (!(update.getMessage().hasPhoto() && client.getAdmin())) {
                return;
            }
        }
        final String text = update.getMessage().getText();
        BotContext context;
        BotState state;
        if (client == null) {
            state = BotState.getInitialState();
            client = new Client(chatId, state.ordinal());
            tools.getClientsService().addUser(client);
            context = BotContext.of(tools, this, client, text);
            state.enter(context);
        } else {
            if (update.getMessage().hasPhoto()) {
                List<PhotoSize> photoSizeList = update.getMessage().getPhoto();
                String textForPhoto = update.getMessage().getCaption() != null ? update.getMessage().getCaption() : "";
                context = BotContext.of(tools, this, client, textForPhoto, photoSizeList);
            } else {
                context = BotContext.of(tools, this, client, text);
            }
            state = BotState.byId(client.getStateId());
        }

        state.handleInput(context);

        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());

        client.setStateId(state.ordinal());
        tools.getClientsService().updateUser(client);
    }


    public void sendMessage(Client client, String text) {
        String chatId = String.valueOf(client.getChatId());
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Client client, String text, StandardKeyboard standardKeyboardRealisation) {
        String chatId = String.valueOf(client.getChatId());
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(standardKeyboardRealisation.getReplyKeyboardMarkup());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(Client client, String pathOfPhoto) {
        File photoFile = new File(pathOfPhoto);

        if (!photoFile.exists()) {
            System.out.println("File not found: " + pathOfPhoto);
            return;
        }

        try (FileInputStream is = new FileInputStream(photoFile)) {
            SendPhoto message = new SendPhoto();
            message.setChatId(Long.toString(client.getChatId()));
            message.setPhoto(new InputFile(is, photoFile.getName()));
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        return super.execute(method);
    }
}

//        if (!update.hasMessage())
//            return;
//
//        final String text = update.getMessage().getText();
//
//        final long chatId = update.getMessage().getChatId();
//        Client client = tools.getClientsService().findByChatId(chatId);
//        System.out.println("here");
//
//        if (text.equals("/orders")) {
//            System.out.println("sdfhsdfhs");
//            List<Orders> orders = tools.getOrderService().findAllByClient(client);
//            if (orders.size() == 0) {
//                sendMessage(update.getMessage().getChatId().toString(), "list is empty !");
//            } else {
//                sendMessage(update.getMessage().getChatId().toString(), orders.stream().toString());
//            }
//                List<String> items = tools.getOrderController().getItems();
//                if(items.size() == 0){
//                    sendMessage(update.getMessage().getChatId().toString(), "list is empty !");
//                }else {
//                    String response = String.join(", ", items);
//                    sendMessage(update.getMessage().getChatId().toString(), response);
//                }
//        }
//    }

//private void sendMessage(String chatId, String text) {
//    SendMessage message = new SendMessage();
//    message.setChatId(chatId);
//    message.setText(text);
//    try {
//        execute(message);
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}