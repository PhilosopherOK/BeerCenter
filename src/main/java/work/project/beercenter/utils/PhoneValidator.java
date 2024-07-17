package work.project.beercenter.utils;

import org.springframework.stereotype.Component;
import work.project.beercenter.model.Client;

import java.util.Random;

@Component
public class PhoneValidator {
    private static Random random = new Random();
    public boolean numberIsValid(Client client, String inputSecretCode){
        try{
            if(client.getVerificationCode().equals(Integer.parseInt(inputSecretCode))){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public void sendRandomCodeFromNumber(Client client, Long number){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(9));
        }
        int secretCode = Integer.parseInt(sb.toString());
        client.setVerificationCode(secretCode);


        //TODO
        client.setVerificationCode(1);
    }
}
