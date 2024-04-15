package work.project.beercenter.utils;

import org.springframework.stereotype.Component;
import work.project.beercenter.model.Clients;

import java.util.Random;

@Component
public class PhoneValidator {
    private static Random random = new Random();
    public boolean numberIsValid(Clients clients, String inputSecretCode){
        try{
            if(clients.getVerificationCode().equals(Integer.parseInt(inputSecretCode))){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public void sendRandomCodeFromNumber(Clients clients, Long number){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(9));
        }
        int secretCode = Integer.parseInt(sb.toString());
        clients.setVerificationCode(secretCode);


        //TODO
        clients.setVerificationCode(1);
    }
}
