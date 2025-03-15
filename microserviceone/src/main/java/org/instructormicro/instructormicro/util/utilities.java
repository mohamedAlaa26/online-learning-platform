package org.instructormicro.instructormicro.util;


import static javax.crypto.Cipher.SECRET_KEY;

public class utilities {

    // Utility method to validate email format
    public static boolean isValidEmail(String email) {
        // Perform email validation (e.g., using regular expressions or a validation library)
        // For simplicity, let's assume a basic email format validation
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }


}
