package work.project.beercenter.utils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public enum KeyboardActionsIterator implements StandardKeyboard {
    GET_KEYBOARD;

    private ReplyKeyboardMarkup replyKeyboardMarkup;

    KeyboardActionsIterator() {
        resetKeyboard();
        createInlineKeyboard(Buttons.NEXT_ACTIONS.getValue(),
                Buttons.PREVIOUS_ACTIONS.getValue(),
                Buttons.BACK.getValue());
        replyKeyboardMarkup = getKeyboard();
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        return replyKeyboardMarkup;
    }
}
