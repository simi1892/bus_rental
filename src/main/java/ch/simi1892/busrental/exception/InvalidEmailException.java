package ch.simi1892.busrental.exception;

import org.springframework.http.HttpStatus;

@ErrorResponse(status = HttpStatus.BAD_REQUEST)
public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Email '" + email + "' has no correct format. Use: abc@xyz.ch");
    }
}
