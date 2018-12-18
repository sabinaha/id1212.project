package server.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(Throwable e) {
        super(e);
    }
}
