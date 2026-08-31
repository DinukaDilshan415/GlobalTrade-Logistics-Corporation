package me.dinuka.gtlc.exception;

//@ApplicationException(rollback=true)
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
