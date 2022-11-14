package tech.jannotti.billing.api.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTool {

    public static void main(String args[]) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = "$2a$10$YY4bi4ahfZxSCvNOzhdcMO2dvwI1E4DgdvL290LLNBsAduXvHFHt.";
        String encoded = passwordEncoder.encode(password);

        System.out.println(encoded);
    }

}
