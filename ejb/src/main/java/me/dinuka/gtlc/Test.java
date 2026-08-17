package me.dinuka.gtlc;

import org.mindrot.jbcrypt.BCrypt;

public class Test {
    public static void main(String[] args) {
        String hashedPassword = BCrypt.hashpw("Admin@1234", BCrypt.gensalt(12));

        System.out.println(hashedPassword);
    }
}
