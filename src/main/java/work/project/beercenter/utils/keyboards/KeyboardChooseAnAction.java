package work.project.beercenter.utils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


public enum KeyboardChooseAnAction implements StandardKeyboard{
    GET_KEYBOARD;

    private ReplyKeyboardMarkup replyKeyboardMarkup;
    KeyboardChooseAnAction() {
        resetKeyboard();
        createInlineKeyboard(Buttons.MY_PROFILE.getValue(),
                Buttons.MY_CARD.getValue(),
                Buttons.MY_BALANCE.getValue(),
                Buttons.UPDATE_MY_PROFILE.getValue(),
                Buttons.MY_ACTIONS.getValue(),
                Buttons.COMPLAINTS_AND_WISHES.getValue(),
                Buttons.LOCATIONS.getValue(),
                Buttons.MY_ORDERS.getValue(),
                Buttons.PRODUCT_ADD.getValue());
        replyKeyboardMarkup = getKeyboard();
    }


    @Override
    public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        return replyKeyboardMarkup;
    }
}
