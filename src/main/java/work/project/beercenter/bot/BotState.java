package work.project.beercenter.bot;

import org.springframework.data.domain.PageRequest;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import work.project.beercenter.mail.NotificationService;
import work.project.beercenter.model.*;
import work.project.beercenter.utils.keyboards.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;


public enum BotState {


    Start {
        @Override
        public void enter(BotContext context) {
            Client client = context.getClient();
            File logotype = new File("src/main/resources/logotypes/main1.jpeg");
            context.getBot().sendPhoto(client, logotype.getAbsolutePath());
            context.getBot().sendMessage(client, "Вітаємо вас у Beer Center !");
        }

        @Override
        public BotState nextState() {
            return EnterFullName;
        }
    },

    EnterFullName {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(), "Будь ласка введіть ваше ПІБ:");
        }

        @Override
        public void handleInput(BotContext context) {
            Client client = context.getClient();
            if (client.getFullName() != null && context.getText().equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                if (context.getText() == null || context.getText().isBlank()) {
                    next = EnterFullName;
                    context.getBot().sendMessage(client, "Будь ласка введіть ваше ПІБ:");
                } else {
                    next = EnterPhone;
                    client.setFullName(context.getText());
                }
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    EnterPhone {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "Будь ласка введіть коректний номер телефону починаючи з 380...:");
        }

        @Override
        public void handleInput(BotContext context) {
            Client client = context.getClient();
            if (client.getPhone() != null && context.getText().equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                String number = context.getText();
                if (!(number == null) && !number.isBlank() && number.length() == 12) {
                    try {
                        Long correctNumber = Long.parseLong(number);
                        context.getBot().sendMessage(client, "Номер телефону коректний");
                        client.setPhone(number);
                        next = EnterEmail;
                    } catch (Exception e) {
                        next = EnterPhone;
                        context.getBot().sendMessage(context.getClient(), "Номер телефону некоректний");
                    }
                } else {
                    next = EnterPhone;
                    context.getBot().sendMessage(context.getClient(), "Номер телефону некоректний");
                }
            }
        }
//        @Override
//        public void handleInput(BotContext context) {
//            String number = context.getInput();
//            Client clients = context.getClient();
//            if (!number.equals("") && number.length() == 12) {
//                try {
//                    Long correctNumber = Long.parseLong(number);
//                    next = CheckForNumberValid;
//                    context.getBot().sendMessage(clients, "номер телефону коректний");
//                    context.getTools().getPhoneValidator().sendRandomCodeFromNumber(clients, correctNumber);
//                    clients.setPhone(context.getInput());
//                } catch (Exception e) {
//                    next = EnterPhone;
//                    context.getBot().sendMessage(context.getClient(), "номер телефону некоректний");
//                }
//            } else {
//                next = EnterPhone;
//                context.getBot().sendMessage(context.getClient(), "номер телефону некоректний");
//            }
//        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

//    CheckForNumberValid {
//        private BotState next;
//
//        @Override
//        public void enter(BotContext context) {
//            context.getBot().sendMessage(context.getClient(),
//                    "Будь ласка введіть код з смс:");
//        }
//
//        @Override
//        public void handleInput(BotContext context) {
//            if (context.getTools().getPhoneValidator().numberIsValid(context.getClient(), context.getText())) {
//                context.getBot().sendMessage(context.getClient(), "номер підтвердженно");
//                next = EnterEmail;
//            } else {
//                context.getBot().sendMessage(context.getClient(), "номер не підтвердженно");
//                next = EnterPhone;
//                context.getClient().setPhone("данні відсутні");
//
//            }
//        }
//
//        @Override
//        public BotState nextState() {
//            return next;
//        }
//    },

    EnterEmail {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "Будь ласка введіть коректну електронну пошту:");
        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getClient().getEmail() != null && context.getText().equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                String email = context.getText();
                if (Utils.isValidEmailAddress(email)) {
                    context.getClient().setEmail(context.getText());
                    next = EnterDateOfBirthday;
                } else {
                    context.getBot().sendMessage(context.getClient(),
                            "Некоректно введена пошта!");
                    next = EnterEmail;
                }
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    EnterDateOfBirthday {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "Будь ласка введіть дату народження у форматі (гггг-мм-дд):");
        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getClient().getDateOfBirthday() != null && context.getText().equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = sdf.parse(context.getText());
                    next = EnterAddress;
                    context.getClient().setDateOfBirthday(date);
                } catch (Exception e) {
                    context.getBot().sendMessage(context.getClient(),
                            "Некоректний формат дати!");
                    next = EnterDateOfBirthday;
                }
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    EnterAddress {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(), "Будь ласка введіть вашу адресу:");
        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getClient().getAddress() != null && context.getText().equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                context.getClient().setAddress(context.getText().equals("") ? "данні відсутні" : context.getText());
                Client client = context.getClient();
                if (client.getLoyaltyCard() == null) {
                    Long newCardNumber = 4444000 + client.getClientId();
                    String getPathBarCode = context.getTools().createBarCodeAndGetPath(newCardNumber);
                    LoyaltyCard loyaltyCard = new LoyaltyCard(0, newCardNumber,
                            getPathBarCode, context.getClient());
                    context.getTools().getLoyaltyCardService().save(loyaltyCard);
                }
                next = MyProfile;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    MyProfile {
        private BotState next;

        @Override
        public boolean isInputNeeded() {
            return false;
        }

        @Override
        public void enter(BotContext context) {
            Client client = context.getClient();
            context.getBot().sendMessage(context.getClient(),
                       "\uD83C\uDFAD" + " Повне ім'я : " + client.getFullName() + "\n" +
                                      "☎" + " Телефон : " + client.getPhone() + "\n" +
                            "\uD83D\uDCE7" + " Електронна пошта: " + client.getEmail() + "\n" +
                            "\uD83C\uDF89" + " Дата народження: " + client.getDateOfBirthday() + "\n" +
                            "\uD83C\uDFE1" + " Домашня адреса: " + client.getAddress());
            next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
        }

        @Override

        public BotState nextState() {
            return next;
        }
    },

    MyCard {
        private BotState next;

        @Override
        public boolean isInputNeeded() {
            return false;
        }

        @Override
        public void enter(BotContext context) {
            //send photo barcode
            Client client = context.getClient();
            String pathOfBarcode = client.getLoyaltyCard().getPathOfBarcode();
            context.getBot().sendPhoto(client, pathOfBarcode);
            context.getBot().sendMessage(client, "\uD83D\uDCB3" + client.getLoyaltyCard().getNumberOfCard());
            next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    MyBalance {
        private BotState next;

        @Override
        public boolean isInputNeeded() {
            return false;
        }

        @Override
        public void enter(BotContext context) {
            Client client = context.getClient();
            context.getBot().sendMessage(client,
                    "\uD83D\uDCB8 " + "Ваш бонусний рахунок становить: " + client.getLoyaltyCard().getBonusBalance());
            next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    UpdateMyProfile {
        @Override
        public boolean isInputNeeded() {
            return false;
        }

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(), "Якщо бажаєте змінити дані то будь ласка," +
                    " дайте відповідь на декілька питань або натисніть назад:", KeyboardBack.GET_KEYBOARD);
        }

        @Override
        public BotState nextState() {
            return EnterFullName;
        }
    },

    Actions {
        private BotState next;
        private boolean inputNeeded = false;

        @Override
        public boolean isInputNeeded() {
            return inputNeeded;
        }

        @Override
        public void enter(BotContext context) {
            List<Action> actions = context.getTools().getActionsService().getAction();
            if (actions.size() == 0) {
                context.getBot().sendMessage(context.getClient(), "Наразі немає акцій");
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                Action action = actions.get(0);
                context.getBot().sendPhoto(context.getClient(), action.getPathOfPhoto());
                context.getBot().sendMessage(context.getClient(), "Акції:" + "\n" + action);
                next = ActionsIterator;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    ActionsIterator {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            if (context.getClient().getAdmin()) {
                context.getBot().sendMessage(context.getClient(),
                        "Оберіть наступний крок з клавіатури команд:", KeyboardAdminActionsIterator.GET_KEYBOARD);
            } else {
                context.getBot().sendMessage(context.getClient(),
                        "Оберіть наступний крок з клавіатури команд:", KeyboardActionsIterator.GET_KEYBOARD);
            }
        }

        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getText();
            if (nextStep.equals(Buttons.NEXT_ACTIONS.getValue())) {
                next = NextAction;
            } else if (nextStep.equals(Buttons.PREVIOUS_ACTIONS.getValue())) {
                next = PreviousAction;
            } else if (nextStep.equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else if (nextStep.equals(Buttons.DELETE.getValue())) {
                List<Action> actions = context.getTools().getActionsService().getAction();
                if (actions.size() == 0) {
                    context.getBot().sendMessage(context.getClient(), "Наразі немає акцій");
                    next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
                } else {
                    context.getTools().getActionsService().deleteById(actions.get(0).getActionsId());
                    context.getBot().sendMessage(context.getClient(), "Акцію успішно видалено");
                }
                next = ActionsIterator;
            } else {
                next = ActionsIterator;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    NextAction {
        private BotState next;


        @Override
        public boolean isInputNeeded() {
            return false;
        }

        @Override
        public void enter(BotContext context) {
            List<Action> actions = context.getTools().getActionsService().getNextActions();
            if (actions.size() == 0) {
                context.getBot().sendMessage(context.getClient(), "Наразі немає акцій");
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                Action action = actions.get(0);
                context.getBot().sendPhoto(context.getClient(), action.getPathOfPhoto());
                context.getBot().sendMessage(context.getClient(), "Акція:" + "\n" + action);
                next = ActionsIterator;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    PreviousAction {
        private BotState next;

        @Override
        public boolean isInputNeeded() {
            return false;
        }

        @Override
        public void enter(BotContext context) {
            List<Action> actions = context.getTools().getActionsService().getPreviousActions();
            if (actions.size() == 0) {
                context.getBot().sendMessage(context.getClient(), "Наразі немає акцій");
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                Action action = actions.get(0);
                context.getBot().sendPhoto(context.getClient(), action.getPathOfPhoto());
                context.getBot().sendMessage(context.getClient(), "Акція:" + "\n" + action);
                next = ActionsIterator;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    ComplaintsAndWishes {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "Будь ласка введіть вашу скаргу чи побажання, або натисніть назад:", KeyboardBack.GET_KEYBOARD);
        }

        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getText();
            if (nextStep.equals(Buttons.BACK.getValue())) {
            } else {
                //send message to admin with complaints or wishes
                Client client = context.getClient();
                NotificationService notificationService = context.getTools().getNotificationService();
                notificationService.sendComplaintsAndWishesToAdminMail(client, context.getText());
            }
            next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

//    Rules {
//        private BotState next;
//        private boolean inputNeeded = false;
//
//        @Override
//        public boolean isInputNeeded() {
//            return inputNeeded;
//        }
//
//        @Override
//        public void enter(BotContext context) {
//            List<Rule> rules = context.getTools().getRulesService().findAll();
//            if (rules.size() == 0) {
//                context.getBot().sendMessage(context.getClient(), "You don't have an rules");
//            } else {
//                for (Rule rule : rules) {
//                    context.getBot().sendMessage(context.getClient(), rule.getName() + "\n" + rule.getDescription());
//                }
//            }
//            next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
//        }
//
//        @Override
//        public BotState nextState() {
//            return next;
//        }
//    },

    Locations {
        private BotState next;
        private boolean inputNeeded = false;

        @Override
        public boolean isInputNeeded() {
            return inputNeeded;
        }

        @Override
        public void enter(BotContext context) {
            List<Location> locations = context.getTools().getLocationsService().findAll();
            if (locations.size() == 0) {
                context.getBot().sendMessage(context.getClient(), "Наразі локації відсутні");
            } else {
                for (Location location : locations) {
                    context.getBot().sendMessage(context.getClient(), "№ " + location.getLocationsId() + "\n" +
                            "Робочі години " + location.getSchedule() + "\n" +
                            "Адреса " + location.getStoreAddress() + "\n" +
                            "Посилання на google maps " + location.getLinkToGoogleMaps());
                }
            }
            next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    MyOrders {
        private BotState next;
        private boolean inputNeeded = false;

        @Override
        public boolean isInputNeeded() {
            return inputNeeded;
        }

        @Override
        public void enter(BotContext context) {
            Client client = context.getClient();

            List<Orders> ordersList = context.getTools().getOrderService().findAllByClient(client);
            if (ordersList.isEmpty()) {
                context.getBot().sendMessage(client, "Наразі замовлення відсутні \uD83D\uDE09",
                        client.getAdmin() ? KeyboardAdminPanel.GET_KEYBOARD : KeyboardChooseAnAction.GET_KEYBOARD);
            } else {
                StringBuilder sb = new StringBuilder("Ваше(і) замовлення:\n");

                Map<String, List<String>> ordersGroupedByReadyTime = new LinkedHashMap<>();
                Map<String, BigDecimal> totalPriceByReadyTime = new LinkedHashMap<>();

                for (Orders order : ordersList) {
                    Product product = context.getTools().getProductService().findById(Long.valueOf(order.getProductId()));
                    BigDecimal productPrice = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(order.getPice()));

                    // групування продуктів по часу
                    String readyTime = order.getDateTimeToReady().toString().replace("T", " at ");
                    String productWithPrice = product.getName() + " " + product.getPrice() + " UAH";

                    ordersGroupedByReadyTime.computeIfAbsent(readyTime, k -> new ArrayList<>()).add(productWithPrice);

                    totalPriceByReadyTime.put(readyTime, totalPriceByReadyTime.getOrDefault(readyTime, BigDecimal.ZERO).add(productPrice));
                }

                for (Map.Entry<String, List<String>> entry : ordersGroupedByReadyTime.entrySet()) {
                    String readyTime = entry.getKey();
                    List<String> products = entry.getValue();

                    for (String productWithPrice : products) {
                        sb.append("Товар: ").append(productWithPrice).append("\n");
                    }

                    sb.append("Готовність о: ").append(readyTime).append("\n")
                            .append("Ціна: ").append(totalPriceByReadyTime.get(readyTime).toString()).append(" UAH\n")
                            .append("--------------------------------------------\n");
                }

                context.getBot().sendMessage(client,
                        sb.toString(),
                        client.getAdmin() ? KeyboardAdminPanel.GET_KEYBOARD : KeyboardChooseAnAction.GET_KEYBOARD);
            }
            next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    LocationAdd {
        private BotState next;


        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "   Будь ласка введіть повну адресу закладу," + "\n" +
                            "посилання на місце розташування закладу," + "\n" +
                            "та години роботи у вигляді (Понеділок-Пятниця 07:00-18:00)," + "\n" +
                            "все розділяючи символом '~'," + "\n" +
                            "або натисніть кнопку назад:", KeyboardBack.GET_KEYBOARD);
        }


        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getText();
            if (nextStep.equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                try {
                    String[] strings = nextStep.split("~");

                    System.out.println(strings);

                    if (strings.length != 3) {
                        throw new Exception("Некоректний ввід !");
                    }

                    Location location = new Location(strings[0], strings[1], strings[2]);

                    System.out.println(location);

                    context.getTools().getLocationsService().save(location);
                    context.getBot().sendMessage(context.getClient(), "Локація успішно додана");
                    next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
                } catch (Exception e) {
                    context.getBot().sendMessage(context.getClient(), e.getMessage());
                    logger.log(Level.SEVERE, "Error handling input in LocationAdd state", e);
                    next = LocationAdd;
                }
            }
        }


        @Override
        public BotState nextState() {
            return next;
        }
    },

    LocationDelete {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            Client client = context.getClient();
            context.getBot().sendMessage(client,
                    "Будь ласка введіть номер закладу, який бажаєте ВИДАЛИТИ, або натисніть кнопку 'Назад'",
                    KeyboardBack.GET_KEYBOARD);
        }

        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getText();
            if (nextStep.equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                try {
                    Long deletedId = Long.parseLong(nextStep);
                    context.getTools().getLocationsService().deleteById(deletedId);
                    next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
                } catch (Exception e) {
                    context.getBot().sendMessage(context.getClient(), "Некоректний ввід !");
                    next = LocationDelete;
                }
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    ActionAdd {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "Будь ласка додайте картинку i підпис до неї починаючи з iм'я акції а далі через  символ '~' опис акції: ",
                    KeyboardBack.GET_KEYBOARD);
        }

        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getText();
            if (nextStep.equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                try {
                    List<PhotoSize> photos = context.getPhotoSizeList();
                    if (photos.isEmpty()) {
                        throw new Exception("Відсутнє фото");
                    }
                    String caption = context.getText() != null ? context.getText() : "";
                    PhotoSize photo = photos.stream()
                            .max(Comparator.comparing(PhotoSize::getFileSize))
                            .orElse(null);

                    String pathForActionImage;
                    pathForActionImage = context.getTools().saveImageAndGetPath(photo.getFileId(), context.getBot(), "actionsImage");
                    Action action = context.getTools().getActionsService().getActionFromPhotoPathAndDescription(pathForActionImage, caption);
                    context.getTools().getActionsService().save(action);
                    next = Actions;

                } catch (Exception e) {
                    context.getBot().sendMessage(context.getClient(), e.getMessage());
                    logger.log(Level.SEVERE, "Error handling input in ProductAdd state", e);
                    next = ActionAdd;
                }
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },


    ProductAdd {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            Client client = context.getClient();
            context.getBot().sendMessage(client,
                    "Будь ласка додайте картинку i підпис до неї починаючи з iм'я товару' а далі через  символ '~' цiну товару цiлим числом: ",
                    KeyboardBack.GET_KEYBOARD);
        }

        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getText();
            if (nextStep.equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                try {
                    List<PhotoSize> photos = context.getPhotoSizeList();
                    if (photos.isEmpty()) {
                        throw new Exception("Вiдсутне фото");
                    }
                    String caption = context.getText() != null ? context.getText() : "";

                    PhotoSize photo = photos.stream()
                            .max(Comparator.comparing(PhotoSize::getFileSize))
                            .orElse(null);

                    if (photo != null) {
                        String pathForActionImage;

                        pathForActionImage = context.getTools().getImbBBService().uploadImageAndGetURL(photo.getFileId(), context.getBot());
                        String[] splitCaption = caption.split("~");

                        Product product = new Product(splitCaption[0],
                                Integer.parseInt(splitCaption[1]), pathForActionImage, new Date());
                        context.getTools().getProductService().save(product);

                        next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;

                    } else {
                        context.getBot().sendMessage(context.getClient(), "Некоректний ввiд !");
                        logger.log(Level.SEVERE, "Error handling input in ProductAdd state", "Некоректний ввiд !");
                        next = ProductAdd;
                    }
                } catch (Exception e) {
                    context.getBot().sendMessage(context.getClient(), "Некоректний ввiд !");
                    logger.log(Level.SEVERE, "Error handling input in ProductAdd state", e);
                    next = ProductAdd;
                }
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    ProductDelete {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            Client client = context.getClient();
            context.getBot().sendMessage(client,
                    "Будь ласка введіть ім'я товару а далі через  символ '~' цiну товару цiлим числом, який бажаєте ВИДАЛИТИ",
                    KeyboardBack.GET_KEYBOARD);
        }

        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getText();
            if (nextStep.equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                String[] split = nextStep.split("~");
                if (split.length == 2) {
                    if (!context.getTools().getProductService().deleteByNameAndPrice(split[0], Integer.parseInt(split[1]))) {
                        context.getBot().sendMessage(context.getClient(), "Введеного товару не існує !");
                        next = ProductDelete;
                    } else {
                        context.getBot().sendMessage(context.getClient(), "Операція успішна !");
                        next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
                    }
                } else {
                    context.getBot().sendMessage(context.getClient(), "Некоректний ввід !");
                    next = ProductDelete;
                }
            }
        }


        @Override
        public BotState nextState() {
            return next;
        }
    },

    MessageForClients {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "сповіщення може бути у формі:  " + "\n" +
                            "картинка + посилання + '~' + підпис" + "\n" +
                            "картинка + підпис" + "\n" +
                            "картинка + посилання" + "\n" +
                            "посилання + '~' + пiдпис" + "\n" +
                            "пiдпис" + "\n" +
                            "або натисніть клавішу Назад",
                    KeyboardBack.GET_KEYBOARD);
        }

        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getText();
            if (nextStep.equals(Buttons.BACK.getValue())) {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            } else {
                try {
                    List<PhotoSize> photos = context.getPhotoSizeList();
                    String caption = context.getText() != null ? context.getText() : "";
                    String[] splitMessage = caption.split("~");

                    final int CLIENTS_PER_PAGE = 20;
                    Long allClients = context.getTools().getClientsService().count();
                    final Long howManyPages = allClients / CLIENTS_PER_PAGE + 1;

                    PhotoSize photo = photos.stream()
                            .max(Comparator.comparing(PhotoSize::getFileSize))
                            .orElse(null);
                    String pathForActionImage = "";
                    if (!photos.isEmpty()) {
                        pathForActionImage = context.getTools().saveImageAndGetPath(photo.getFileId(), context.getBot(), "actionsImage");
                    }

                    for (int i = 0; i < howManyPages; i++) {
                        List<Client> clientListForNotification = context.getTools().getClientsService().findAll(PageRequest.of(i, CLIENTS_PER_PAGE));
                        for (Client client : clientListForNotification) {
                            if (!photos.isEmpty()) {
                                context.getBot().sendPhoto(client, pathForActionImage);
                            }
                            for (int j = 0; j < splitMessage.length; j++) {
                                context.getBot().sendMessage(client, splitMessage[j]);
                            }
                        }
                    }
                    next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;

                } catch (Exception e) {
                    context.getBot().sendMessage(context.getClient(), "Некоректний ввiд !");
                    logger.log(Level.SEVERE, "Error handling input in MessageForClients state", e);
                    next = MessageForClients;
                }
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    ChooseAnAction {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "Будь ласка оберіть необхідну операцію з клавіатури команд:", KeyboardChooseAnAction.GET_KEYBOARD);
        }

        @Override
        public void handleInput(BotContext context) {
            String command = context.getText();
            if (command.equals(Buttons.MY_PROFILE.getValue())) {
                next = MyProfile;
            } else if (command.equals(Buttons.MY_CARD.getValue())) {
                next = MyCard;
            } else if (command.equals(Buttons.MY_BALANCE.getValue())) {
                next = MyBalance;
            } else if (command.equals(Buttons.UPDATE_MY_PROFILE.getValue())) {
                next = UpdateMyProfile;
            } else if (command.equals(Buttons.MY_ACTIONS.getValue())) {
                next = Actions;
            } else if (command.equals(Buttons.COMPLAINTS_AND_WISHES.getValue())) {
                next = ComplaintsAndWishes;
            } else if (command.equals(Buttons.LOCATIONS.getValue())) {
                next = Locations;
            } else if (command.equals(Buttons.MY_ORDERS.getValue())) {
                next = MyOrders;
            } else {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    // ADMIN MANAGEMENT

    Admin_Panel {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "Будь ласка оберіть необхідну операцію з клавіатури команд:", KeyboardAdminPanel.GET_KEYBOARD);
        }

        @Override
        public void handleInput(BotContext context) {
            String command = context.getText();
            if (command.equals(Buttons.MY_PROFILE.getValue())) {
                next = MyProfile;
            } else if (command.equals(Buttons.MY_CARD.getValue())) {
                next = MyCard;
            } else if (command.equals(Buttons.MY_BALANCE.getValue())) {
                next = MyBalance;
            } else if (command.equals(Buttons.UPDATE_MY_PROFILE.getValue())) {
                next = UpdateMyProfile;
            } else if (command.equals(Buttons.MY_ACTIONS.getValue())) {
                next = Actions;
            } else if (command.equals(Buttons.COMPLAINTS_AND_WISHES.getValue())) {
                next = ComplaintsAndWishes;
            } else if (command.equals(Buttons.LOCATIONS.getValue())) {
                next = Locations;
            } else if (command.equals(Buttons.MY_ORDERS.getValue())) {
                next = MyOrders;
            } else if (command.equals(Buttons.ACTION_ADD.getValue())) {
                next = ActionAdd;
            } else if (command.equals(Buttons.PRODUCT_ADD.getValue())) {
                next = ProductAdd;
            } else if (command.equals(Buttons.PRODUCT_DELETE.getValue())) {
                next = ProductDelete;
            } else if (command.equals(Buttons.LOCATIONS_ADD.getValue())) {
                next = LocationAdd;
            } else if (command.equals(Buttons.LOCATIONS_DELETE.getValue())) {
                next = LocationDelete;
            }else if (command.equals(Buttons.MESSAGE_FOR_CLIENTS.getValue())) {
                next = MessageForClients;
            } else {
                next = context.getClient().getAdmin() ? Admin_Panel : ChooseAnAction;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    Approved(false) {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClient(),
                    "Thank you for application!");
        }

        @Override
        public BotState nextState() {
            return Start;
        }
    };

    // --------------- //
    private static final Logger logger = Logger.getLogger(BotState.class.getName());
    private static BotState[] states;
    private final boolean inputNeeded;

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }

        return states[id];
    }

    public boolean isInputNeeded() {
        return inputNeeded;
    }

    public void handleInput(BotContext context) {
        // do nothing by default
    }

    public abstract void enter(BotContext context);

    public abstract BotState nextState();

    static {
        try {
            FileHandler fileHandler = new FileHandler("bot_errors.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to set up logger", e);
        }
    }
}
