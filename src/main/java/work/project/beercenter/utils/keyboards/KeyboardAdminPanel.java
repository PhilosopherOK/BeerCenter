package work.project.beercenter.utils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public enum KeyboardAdminPanel implements StandardKeyboard{
    GET_KEYBOARD;

    private ReplyKeyboardMarkup replyKeyboardMarkup;
    KeyboardAdminPanel() {
        resetKeyboard();
        createInlineKeyboard(Buttons.MY_PROFILE.getValue(),
                Buttons.MY_CARD.getValue(),
                Buttons.MY_BALANCE.getValue(),
                Buttons.UPDATE_MY_PROFILE.getValue(),
                Buttons.MY_ACTIONS.getValue(),
                Buttons.COMPLAINTS_AND_WISHES.getValue(),
                Buttons.LOCATIONS.getValue(),
                Buttons.MY_ORDERS.getValue(),
                Buttons.LOCATIONS_ADD.getValue(),
                Buttons.LOCATIONS_DELETE.getValue(),
                Buttons.ACTION_ADD.getValue(),
                Buttons.PRODUCT_ADD.getValue(),
                Buttons.PRODUCT_DELETE.getValue(),
                Buttons.MESSAGE_FOR_CLIENTS.getValue()
                );
        replyKeyboardMarkup = getKeyboard();
    }


    @Override
    public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        return replyKeyboardMarkup;
    }
}