package politechnika.lodzka.qrcode;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {
    private final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final static Random rng = new SecureRandom();
    public final static Integer SAVE_LENGTH = 32;
    public final static Character SPACE_CHAR = '_';

    private static char randomChar(){
        return ALPHABET.charAt(rng.nextInt(ALPHABET.length()));
    }

    public static String randomUUID(int length, int spacing, char spacerChar){
        StringBuilder sb = new StringBuilder();
        int spacer = 0;
        while(length > 0){
            if(spacer == spacing){
                sb.append(spacerChar);
                spacer = 0;
            }
            length--;
            spacer++;
            sb.append(randomChar());
        }
        return sb.toString();
    }
}
