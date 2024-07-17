package work.project.beercenter.utils.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Buttons {
    NEXT_ACTIONS("➡ " + "NextAction"),
    PREVIOUS_ACTIONS("⬅ " + "PreviousAction"),
    BACK("\uD83D\uDD19 " + "Back"),
    MY_PROFILE("\uD83E\uDDB8 " + "MyProfile"),
    MY_CARD("\uD83D\uDCB3 " + "MyCard"),
    MY_BALANCE("\uD83D\uDCB8 " + "MyBalance"),
    UPDATE_MY_PROFILE("\uD83D\uDCDD " + "UpdateMyProfile"),
    MY_ACTIONS("\uD83C\uDF8A " + "MyActions"),
    COMPLAINTS_AND_WISHES("\uD83D\uDCEC " + "ComplaintsAndWishes"),
    MY_ORDERS("\uD83C\uDF81 " + "MyOrders"),
    //TODO add MY_LOCATION  and MY_RULE
    DELETE("\uD83D\uDEAB " + "Delete"),
    ACTION_ADD("🌶" + "ActionAdd"),
    ACTION_MANAGEMENT("\uD83C\uDF81" + "ActionManagement"),
    MENU_MANAGEMENT("\uD83C\uDF7B" + "MenuManagement"),
    LOCATIONS("⛺" + "Locations"),
    LOCATIONS_ADD("⛺" + "LocationsAdd"),
    RULE_MANAGEMENT("🔞" + "RuleManagement"),
    PRODUCT_ADD("\uD83C\uDF7A " + "ProductAdd"),
    PRODUCT_DELETE("\uD83E\uDED7 " + "ProductDelete"),;
    private final String value;
}
