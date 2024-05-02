package ch.simi1892.busrental.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Email '" + email + "' has no correct format. Use: abc@xyz.ch");
    }
}
