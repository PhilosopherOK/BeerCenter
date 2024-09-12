package work.project.beercenter.utils.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;

//https://emojigraph.org/ru/seedling/

@Getter
@AllArgsConstructor
public enum Buttons {
    NEXT_ACTIONS("➡ " + "Наступна Акція"),
    PREVIOUS_ACTIONS("⬅ " + "Попередня Акція"),
    BACK("\uD83D\uDD19 " + "Назад"),
    MY_PROFILE("\uD83E\uDDB8 " + "Профіль"),
    MY_CARD("\uD83D\uDCB3 " + "Карта"),
    MY_BALANCE("\uD83D\uDCB8 " + "Баланс"),
    UPDATE_MY_PROFILE("\uD83D\uDCDD " + "Оновити профіль"),
    MY_ACTIONS("\uD83C\uDF8A " + "Акції"),
    COMPLAINTS_AND_WISHES("\uD83D\uDCEC " + "Скарги і побажання"),
    LOCATIONS("⛺" + "Локації"),
    MY_ORDERS("\uD83C\uDF81 " + "Мої замовлення"),

    //TODO add MY_RULE
    DELETE("\uD83D\uDEAB " + "Видалити"),
    ACTION_ADD("🌶" + "Додати акцію"),
    MENU_MANAGEMENT("\uD83C\uDF7B" + "Меню управління"),
    LOCATIONS_ADD("\uD83C\uDF31" + "Додати локацію"),
    LOCATIONS_DELETE("\uD83E\uDE93" + "Видалити локацію"),
//    RULE_MANAGEMENT("🔞" + "Меню правил"),
    PRODUCT_ADD("\uD83C\uDF7A " + "Додати продукт"),
    PRODUCT_DELETE("\uD83E\uDED7 " + "Видалити продукт"),
    MESSAGE_FOR_CLIENTS( "\uD83D\uDC8C" + "Повідомлення для клієнтів"),
    ;
    private final String value;
}
