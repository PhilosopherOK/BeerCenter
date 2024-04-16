package work.project.beercenter.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import lombok.Getter;
import org.springframework.stereotype.Component;
import work.project.beercenter.administration.AdminCommand;
import work.project.beercenter.mail.NotificationService;
import work.project.beercenter.service.*;

import java.io.File;
import java.io.IOException;

@Getter
@Component
public class Tools {
    private final ClientsService clientsService;
    private final ActionsService actionsService;
    private final LocationsService locationsService;
    private final LoyaltyCardService loyaltyCardService;
    private final MenusService menusService;
    private final RulesService rulesService;
    private final NotificationService notificationService;
    private final AdminCommand adminCommand;
    private final PhoneValidator phoneValidator;
    private Long startingCardNumber = 100000l;

    public Tools(ClientsService clientsService, ActionsService actionsService,
                 LocationsService locationsService, LoyaltyCardService loyaltyCardService,
                 MenusService menusService, RulesService rulesService,
                 NotificationService notificationService, AdminCommand adminCommand,
                 PhoneValidator phoneValidator) {
        this.clientsService = clientsService;
        this.actionsService = actionsService;
        this.locationsService = locationsService;
        this.loyaltyCardService = loyaltyCardService;
        this.menusService = menusService;
        this.rulesService = rulesService;
        this.notificationService = notificationService;
        this.adminCommand = adminCommand;
        this.phoneValidator = phoneValidator;
    }

    public synchronized Long getNewCardNumber() {
        return loyaltyCardService.count() + startingCardNumber++;
    }

    public String createBarCodeAndGetPath(Long cardNumber) {
        String barcodeText = cardNumber.toString();
        String filePath = "barcodes/barcode" + cardNumber + ".png";
        String neededPath = "src/main/resources/";
        int width = 300;
        int height = 100;

        try {
            BitMatrix bitMatrix = new Code128Writer().encode(barcodeText, BarcodeFormat.CODE_128, width, height, null);
            File outputFile = new File(neededPath + filePath);
            MatrixToImageWriter.writeToFile(bitMatrix, "PNG", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
