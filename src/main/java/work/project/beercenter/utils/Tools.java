package work.project.beercenter.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import work.project.beercenter.administration.AdminCommand;
import work.project.beercenter.bot.ChatBot;
import work.project.beercenter.mail.NotificationService;
import work.project.beercenter.service.*;
import work.project.beercenter.service.utils.ImbBBService;


import java.io.*;
import java.net.URL;


@Getter
@RequiredArgsConstructor
@Component
public class Tools {
    private final ClientsService clientsService;
    private final ActionsService actionsService;
    private final LocationsService locationsService;
    private final LoyaltyCardService loyaltyCardService;
    private final ProductService productService;
    private final RulesService rulesService;
    private final NotificationService notificationService;
    private final OrderService orderService;
    private final AdminCommand adminCommand;
    private final PhoneValidator phoneValidator;
    private final ImbBBService imbBBService;

    @Value("${main.path.for.images}")
    private String mainPathForImages;


    public String createBarCodeAndGetPath(Long cardNumber) {
        String barcodeText = cardNumber.toString();
        String filePath = "barcodes/barcode" + cardNumber + ".png";
        int width = 300;
        int height = 100;
        File outputFile = null;
        File parentDir = new File(mainPathForImages + "/" + filePath).getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try {
            BitMatrix bitMatrix = new Code128Writer().encode(barcodeText, BarcodeFormat.CODE_128, width, height, null);
            outputFile = new File(mainPathForImages + filePath);
            MatrixToImageWriter.writeToFile(bitMatrix, "PNG", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile.getAbsolutePath();
    }

//    public String saveImageAndGetPath(File file){
//        if (file.isEmpty()) {
//            throw new IllegalArgumentException("Cannot upload an empty file");
//        }
//        if (!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpeg")) {
//            throw new IllegalArgumentException("File should be png/jpeg ");
//        }
//        long maxFileSize = 10 * 1024 * 1024; // 10 MB in bytes
//        if (file.getSize() > maxFileSize) {
//            throw new IllegalArgumentException("File size exceeds 10 MB limit");
//        }
//
//        File savedFile = new File("src/main/resources/action" + file.getName());
//        File parentDir = savedFile.getParentFile();
//        if (parentDir != null && !parentDir.exists()) {
//            parentDir.mkdirs();
//        }
//        try (InputStream inputStream = new FileInputStream(file);
//            OutputStream outputStream = new FileOutputStream(savedFile)){
//            inputStream.transferTo(outputStream);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return savedFile.getAbsolutePath();
//    }


    public String saveImageAndGetPath(String fileId, ChatBot bot, String nameOfEventFolder) throws Exception {
        // Получить путь к файлу на серверах Telegram
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);

        org.telegram.telegrambots.meta.api.objects.File filePath = bot.execute(getFile);
        String fileUrl = "https://api.telegram.org/file/bot" + bot.getBotToken() + "/" + filePath.getFilePath();

        // Установить путь для сохранения файла
        File savedFile = new File(mainPathForImages + nameOfEventFolder + "/" + filePath.getFilePath().substring(filePath.getFilePath().lastIndexOf("/") + 1));
        File parentDir = savedFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        // Сохраняем файл из телеграм хранилища в локальную папку
        URL url = new URL(fileUrl);
        try (InputStream in = url.openStream(); OutputStream out = new FileOutputStream(savedFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to download and save file: " + e.getMessage());
        }
        return savedFile.getAbsolutePath();
    }

}
