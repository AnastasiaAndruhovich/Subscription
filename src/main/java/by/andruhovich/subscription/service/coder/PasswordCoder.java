package by.andruhovich.subscription.service.coder;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordCoder {
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}
