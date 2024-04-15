package work.project.beercenter.bot;


import lombok.Getter;
import work.project.beercenter.model.Clients;
import work.project.beercenter.utils.Tools;
@Getter
public class BotContext {
    private final Tools tools;
    private final ChatBot bot;
    private final Clients clients;
    private final String input;

    public static BotContext of(Tools tools, ChatBot bot, Clients clients, String text) {
        return new BotContext(tools, bot, clients, text);
    }

    private BotContext(Tools tools, ChatBot bot, Clients clients, String input) {
        this.tools = tools;
        this.bot = bot;
        this.clients = clients;
        this.input = input;
    }

}
