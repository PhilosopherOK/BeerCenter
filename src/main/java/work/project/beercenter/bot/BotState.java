package work.project.beercenter.bot;

import work.project.beercenter.mail.NotificationService;
import work.project.beercenter.model.Clients;
import work.project.beercenter.model.LoyaltyCard;

import java.text.SimpleDateFormat;

import java.util.Date;

public enum BotState {

    Start {
        @Override
        public void enter(BotContext context) {
            Clients clients = context.getClients();
            context.getBot().sendPhoto(clients, "logotypes/main1.jpeg");
            context.getBot().sendMessage(clients, "Вітаємо вас у Beer Center !");
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
            context.getBot().sendMessage(context.getClients(), "Будь ласка введіть ваше ПІБ:");
        }

        @Override
        public void handleInput(BotContext context) {
            Clients clients = context.getClients();
            if (context.getInput().equals("")) {
                next = EnterFullName;
                context.getBot().sendMessage(clients, "Будь ласка введіть ваше ПІБ:");
            } else {
                next = EnterPhone;
                clients.setFullName(context.getInput());
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
            context.getBot().sendMessage(context.getClients(),
                    "Будь ласка введіть коректний номер телефону починаючи з 380(вам надійде смс з кодом підтвердження):");
        }

        @Override
        public void handleInput(BotContext context) {
            String number = context.getInput();
            Clients clients = context.getClients();
            if (!number.equals("") && number.length() == 11) {
                try {
                    Long correctNumber = Long.parseLong(number);
                    next = CheckForNumberValid;
                    context.getBot().sendMessage(clients, "номер телефону коректний");
                    context.getTools().getPhoneValidator().sendRandomCodeFromNumber(clients, correctNumber);
                    clients.setPhone(context.getInput());
                } catch (Exception e) {
                    next = EnterPhone;
                    context.getBot().sendMessage(context.getClients(), "номер телефону некоректний");
                }
            } else {
                next = EnterPhone;
                context.getBot().sendMessage(context.getClients(), "номер телефону некоректний");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    CheckForNumberValid {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(),
                    "Будь ласка введіть код з смс:");
        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getTools().getPhoneValidator().numberIsValid(context.getClients(), context.getInput())) {
                context.getBot().sendMessage(context.getClients(), "номер підтвердженно");
                next = EnterEmail;
            } else {
                context.getBot().sendMessage(context.getClients(), "номер не підтвердженно");
                next = EnterPhone;
                context.getClients().setPhone("данні відсутні");

            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    EnterEmail {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(),
                    "Будь ласка введіть коректну електронну пошту:");
        }

        @Override
        public void handleInput(BotContext context) {
            String email = context.getInput();

            if (Utils.isValidEmailAddress(email)) {
                context.getClients().setEmail(context.getInput());
                next = EnterDateOfBirthday;
            } else {
                context.getBot().sendMessage(context.getClients(),
                        "Некоректно введена пошта!");
                next = EnterEmail;
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
            context.getBot().sendMessage(context.getClients(),
                    "Будь ласка введіть дату народження у форматі (гггг-мм-дд):");
        }

        @Override
        public void handleInput(BotContext context) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(context.getInput());
                next = EnterAddress;
                context.getClients().setDateOfBirthday(date);
            } catch (Exception e) {
                context.getBot().sendMessage(context.getClients(),
                        "Некоректний формат дати!");
                next = EnterDateOfBirthday;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    EnterAddress {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(), "Будь ласка введіть вашу адресу:");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getClients().setAddress(context.getInput().equals("") ? "данні відсутні" : context.getInput());
            Clients clients = context.getClients();
            if (clients.getLoyaltyCard() == null) {
                Long newCardNumber = context.getTools().getNewCardNumber();
                String getPathBarCode = context.getTools().createBarCodeAndGetPath(newCardNumber);
                LoyaltyCard loyaltyCard = new LoyaltyCard(0, newCardNumber,
                        getPathBarCode, context.getClients());
                context.getTools().getLoyaltyCardService().save(loyaltyCard);
            }
        }

        @Override
        public BotState nextState() {
            return MyProfile;
        }
    },
    ChooseAnAction {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(),
                    "Будь ласка оберіть необхідну операцію з клавіатури операцій:");
        }

        @Override
        public void handleInput(BotContext context) {
            String command = context.getInput();
            switch (command) {
                case "/myProfile":
                    next = MyProfile;
                    break;
                case "/myCard":
                    next = MyCard;
                    break;
                case "/myBalance":
                    next = MyBalance;
                    break;
                case "/updateMyProfile":
                    next = UpdateMyProfile;
                    break;
                case "/myActions":
                    next = MyActions;
                    break;
                case "/complaintsAndWishes":
                    next = ComplaintsAndWishes;
                    break;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    MyProfile {
        @Override
        public void enter(BotContext context) {
            Clients clients = context.getClients();
            context.getBot().sendMessage(context.getClients(),
                    "🤩" + "\uD83E\uDD29 Your full name is: " + clients.getFullName() + "\n" +
                            "📱" + "\uD83D\uDCF1 Your phone is: " + clients.getPhone() + "\n" +
                            "📧" + "\uD83D\uDCE7 Your email is: " + clients.getEmail() + "\n" +
                            "🎂" + "\uD83C\uDF82 Your dateOfBirthday is: " + clients.getDateOfBirthday() + "\n" +
                            "🌃" + "\uD83C\uDF03 Your address is: " + clients.getAddress());
        }

        @Override
        public BotState nextState() {
            return ChooseAnAction;
        }
    },

    MyCard {
        @Override
        public void enter(BotContext context) {
            //send photo barcode
            Clients clients = context.getClients();
            String pathOfBarcode = clients.getLoyaltyCard().getPathOfBarcode();
            context.getBot().sendPhoto(clients, pathOfBarcode);
            context.getBot().sendMessage(clients,
                    "ви найдивовіжніша людина, а ось ваш номер бонусної карти :" + "\n" +
                            "💳" + "\uD83D\uDCB3 " + clients.getLoyaltyCard().getNumberOfCard());
        }

        @Override
        public BotState nextState() {
            return ChooseAnAction;
        }
    },

    MyBalance {
        @Override
        public void enter(BotContext context) {
            Clients clients = context.getClients();
            context.getBot().sendMessage(clients,
                    "\uD83E\uDD11 ваш бонусний рахунок становить: " + clients.getLoyaltyCard().getBonusBalance());
        }

        @Override
        public BotState nextState() {
            return ChooseAnAction;
        }
    },

    UpdateMyProfile {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(), "Якщо бажаете змінити дані то будь ласта дайте відповідь на декілька питань:");
        }

        @Override
        public BotState nextState() {
            return EnterFullName;
        }
    },

    MyActions {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(), "Ваші акції:" + "\n" +
                    context.getTools().getActionsService().getActions());
        }

        @Override
        public BotState nextState() {
            return ActionsIterator;
        }
    },
    ActionsIterator {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(),
                    "Оберіть наступний крок з клавіатури команд:");
        }

        @Override
        public void handleInput(BotContext context) {
            String nextStep = context.getInput();
            switch (nextStep) {
                case "/next":
                    next = NextAction;
                    break;
                case "/previous":
                    next = PreviousAction;
                case "/back":
                    next = ChooseAnAction;
                    break;
                default:
                    next = ChooseAnAction;
                    break;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    NextAction {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(),
                    context.getTools().getActionsService().getNextActions().toString());
        }

        @Override
        public BotState nextState() {
            return ActionsIterator;
        }
    },
    PreviousAction {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(),
                    context.getTools().getActionsService().getPreviousActions().toString());
        }

        @Override
        public BotState nextState() {
            return ActionsIterator;
        }
    },

    ComplaintsAndWishes {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(),
                    "Будь ласка введіть вашу скаргу чи побажання: ");
        }

        @Override
        public void handleInput(BotContext context) {
            Clients clients = context.getClients();
            NotificationService notificationService = context.getTools().getNotificationService();

            //send message to admin with complaints or wishes
            notificationService.sendComplaintsAndWishesToAdminMail(clients, context.getInput());
        }

        @Override
        public BotState nextState() {
            return MyProfile;
        }
    },


    Approved(false) {
        @Override
        public void enter(BotContext context) {
            context.getBot().sendMessage(context.getClients(),
                    "Thank you for application!");
        }

        @Override
        public BotState nextState() {
            return Start;
        }
    };

    // --------------- //

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
}
