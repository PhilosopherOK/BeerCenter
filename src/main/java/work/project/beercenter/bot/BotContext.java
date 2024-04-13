package work.project.beercenter.bot;


import work.project.beercenter.model.Client;

public class BotContext {
    private final ChatBot bot;
    private final Client client;
    private final String input;

    public static BotContext of(ChatBot bot, Client client, String text) {
        return new BotContext(bot, client, text);
    }

    private BotContext(ChatBot bot, Client client, String input) {
        this.bot = bot;
        this.client = client;
        this.input = input;
    }

    public ChatBot getBot() {
        return bot;
    }

    public Client getUser() {
        return client;
    }

    public String getInput() {
        return input;
    }
}
