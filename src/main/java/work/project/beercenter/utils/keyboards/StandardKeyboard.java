package work.project.beercenter.utils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface StandardKeyboard {
    default public void resetKeyboard(){
        KeyboardUtils.resetKeyboard();
    }
    default public ReplyKeyboardMarkup getKeyboard(){
        return KeyboardUtils.getKeyboard();
    }

    default public ReplyKeyboardMarkup createInlineKeyboard(String... buttonTexts){
        return KeyboardUtils.createReplyKeyboard(buttonTexts);
    }

    default public void addButton(String buttonText){
        KeyboardUtils.addButton(buttonText);
    }

    public ReplyKeyboardMarkup getReplyKeyboardMarkup();


}
