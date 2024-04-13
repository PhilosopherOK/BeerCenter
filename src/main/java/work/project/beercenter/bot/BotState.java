package work.project.beercenter.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import work.project.beercenter.model.Client;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum BotState {

    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Hello!");
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
            sendMessage(context, "Please enter your full name:");
        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getInput().equals("")) {
                next = EnterFullName;
                sendMessage(context, "Please enter your full name:");
            } else {
                next = EnterPhone;
                context.getUser().setFullName(context.getInput());
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    EnterPhone {
        //TODO need to make phone number checker like validator!!!
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Please enter your phone:");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setPhone(context.getInput());
        }

        @Override
        public BotState nextState() {
            return EnterEmail;
        }
    },
    EnterEmail {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Enter your e-mail please:");
        }

        @Override
        public void handleInput(BotContext context) {
            String email = context.getInput();

            if (Utils.isValidEmailAddress(email)) {
                context.getUser().setEmail(context.getInput());
                next = Approved;
            } else {
                sendMessage(context, "Wrong e-mail address!");
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
            sendMessage(context, "Please enter your date of birthday in format like (yyyy-MM-dd):");
        }

        @Override
        public void handleInput(BotContext context) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(context.getInput());
                next = EnterAddress;
                context.getUser().setDateOfBirthday(date);
            } catch (Exception e) {
                sendMessage(context, "Wrong date format!");
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
            sendMessage(context, "Please enter your address:");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setAddress(context.getInput());
        }

        @Override
        public BotState nextState() {
            return EnterEmail;
        }
    },
    ChooseAnAction {
        private BotState next;


        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Please choose an action from keyboard:");
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
                    next = MyActions; //TODO next, previous, exit
                    break;
                case "/callMe":
                    next = CallMe;
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
            Client client = context.getUser();
            sendMessage(context,
                    "🤩" + "\uD83E\uDD29 Your full name is: " + client.getFullName() + "\n" +
                            "📱" + "\uD83D\uDCF1 Your phone is: " + client.getPhone() + "\n" +
                            "📧" + "\uD83D\uDCE7 Your email is: " + client.getEmail() + "\n" +
                            "🎂" + "\uD83C\uDF82 Your dateOfBirthday is: " + client.getDateOfBirthday() + "\n" +
                            "🌃" + "\uD83C\uDF03 Your address is: " + client.getAddress());

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
            Client client = context.getUser();
            String pathOfBarcode = client.getLoyaltyCard().getPathOfBarcode();
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream(pathOfBarcode);

            SendPhoto message = new SendPhoto();
            message.setChatId(Long.toString(client.getChatId()));
            message.setPhoto(new InputFile(is, pathOfBarcode));
            try {
                context.getBot().execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            //TODO get balance in any class
            sendMessage(context,
                    "🤑" + "\uD83E\uDD11 Your balance of bonus is: " + client.getLoyaltyCard().getBonusBalance() + "\n" +
                            "💳" + "\uD83D\uDCB3 Your address is: " + client.getLoyaltyCard().getNumberOfCard());

        }

        @Override
        public BotState nextState() {
            return ChooseAnAction;
        }
    },

    MyProfile {
        @Override
        public void enter(BotContext context) {
            Client client = context.getUser();
            sendMessage(context,
                    "🤩" + "\uD83E\uDD29 Your full name is: " + client.getFullName() + "\n" +
                            "📱" + "\uD83D\uDCF1 Your phone is: " + client.getPhone() + "\n" +
                            "📧" + "\uD83D\uDCE7 Your email is: " + client.getEmail() + "\n" +
                            "🎂" + "\uD83C\uDF82 Your dateOfBirthday is: " + client.getDateOfBirthday() + "\n" +
                            "🤑" + "\uD83E\uDD11 Your balance is: " + client.getLoyaltyCard().getBonusBalance() + "\n" +
                            "🌃" + "\uD83C\uDF03 Your address is: " + client.getAddress());

        }

        @Override
        public BotState nextState() {
            return ChooseAnAction;
        }
    },

    MyProfile {
        @Override
        public void enter(BotContext context) {
            Client client = context.getUser();
            sendMessage(context,
                    "🤩" + "\uD83E\uDD29 Your full name is: " + client.getFullName() + "\n" +
                            "📱" + "\uD83D\uDCF1 Your phone is: " + client.getPhone() + "\n" +
                            "📧" + "\uD83D\uDCE7 Your email is: " + client.getEmail() + "\n" +
                            "🎂" + "\uD83C\uDF82 Your dateOfBirthday is: " + client.getDateOfBirthday() + "\n" +
                            "🤑" + "\uD83E\uDD11 Your balance is: " + client.getLoyaltyCard().getBonusBalance() + "\n" +
                            "🌃" + "\uD83C\uDF03 Your address is: " + client.getAddress());

        }

        @Override
        public BotState nextState() {
            return ChooseAnAction;
        }
    },

    Approved(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Thank you for application!");
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

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(Long.toString(context.getUser().getChatId()));
        message.setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
