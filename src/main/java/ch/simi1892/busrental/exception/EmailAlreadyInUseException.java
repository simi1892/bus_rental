package ch.simi1892.busrental.exception;

import org.springframework.http.HttpStatus;

@ErrorResponse(status = HttpStatus.CONFLICT)
public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {
        super("Email already in use: " + email);
    }
}