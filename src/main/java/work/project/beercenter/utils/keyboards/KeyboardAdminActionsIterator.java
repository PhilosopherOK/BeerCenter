package work.project.beercenter.utils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public enum KeyboardAdminActionsIterator implements StandardKeyboard {
    GET_KEYBOARD;

    private ReplyKeyboardMarkup replyKeyboardMarkup;

    KeyboardAdminActionsIterator() {
        resetKeyboard();
        createInlineKeyboard(Buttons.NEXT_ACTIONS.getValue(),
                Buttons.PREVIOUS_ACTIONS.getValue(),
                Buttons.BACK.getValue(),
                Buttons.DELETE.getValue());
        replyKeyboardMarkup = getKeyboard();
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        return replyKeyboardMarkup;
    }
}
