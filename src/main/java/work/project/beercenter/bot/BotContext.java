package work.project.beercenter.bot;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import work.project.beercenter.model.Client;
import work.project.beercenter.utils.Tools;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BotContext {
    private final Tools tools;
    private final ChatBot bot;
    private final Client client;
    private final String text;
    private final List<PhotoSize> photoSizeList;
    public static BotContext of(Tools tools, ChatBot bot, Client client, String text) {
        return new BotContext(tools, bot, client, text, null);
    }

    public static BotContext of(Tools tools, ChatBot bot, Client client, String text, List<PhotoSize> photoSizeList) {
        return new BotContext(tools, bot, client, text, photoSizeList);
    }
}
