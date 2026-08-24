package me.dinuka.gtlc;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void main(String[] args) {
        String hashedPassword = BCrypt.hashpw("Admin@1234", BCrypt.gensalt(12));

        System.out.println(hashedPassword);

        // 1. Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // 2. Define the pattern matching your format (yyMMdd-HHmmss)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd-HHmmss");

        // 3. Format the date and prepend "C-"
        String caseNumber = "C-" + now.format(formatter);

        // Output the result
        System.out.println(caseNumber);
    }
}
