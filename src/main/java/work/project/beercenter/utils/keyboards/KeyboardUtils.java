package work.project.beercenter.utils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtils {

    private static final int MAX_BUTTONS_PER_ROW = 3;
    private static final int MAX_CHARACTERS_PER_BUTTON = 35;

    private static List<KeyboardRow> keyboard = new ArrayList<>();
    private static KeyboardRow currentRow = new KeyboardRow();
    private static int currentRowLength = 0;

    public static void addButton(String buttonText) {
        if (currentRowLength + buttonText.length() > MAX_CHARACTERS_PER_BUTTON || currentRow.size() == MAX_BUTTONS_PER_ROW) {
            keyboard.add(currentRow);
            currentRow = new KeyboardRow();
            currentRowLength = 0;
        }
        KeyboardButton button = new KeyboardButton(buttonText);
        currentRow.add(button);
        currentRowLength += buttonText.length();
    }

    public static ReplyKeyboardMarkup getKeyboard() {
        if (!currentRow.isEmpty()) {
            keyboard.add(currentRow);
            currentRow = new KeyboardRow();
            currentRowLength = 0;
        }

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public static void resetKeyboard() {
        keyboard = new ArrayList<>();
        currentRow = new KeyboardRow();
        currentRowLength = 0;
    }

    public static ReplyKeyboardMarkup createReplyKeyboard(String... buttonTexts) {
        resetKeyboard();
        for (String buttonText : buttonTexts) {
            addButton(buttonText);
        }
        return getKeyboard();
    }
}