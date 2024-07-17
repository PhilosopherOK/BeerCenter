package work.project.beercenter.utils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public enum KeyboardBack implements StandardKeyboard{
    GET_KEYBOARD;

    private ReplyKeyboardMarkup replyKeyboardMarkup;
    KeyboardBack() {
        resetKeyboard();
        createInlineKeyboard(Buttons.BACK.getValue());
        replyKeyboardMarkup = getKeyboard();
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        return replyKeyboardMarkup;
    }
}