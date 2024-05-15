package ch.simi1892.busrental;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "a";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}

