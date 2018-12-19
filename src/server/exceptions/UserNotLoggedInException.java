package server.exceptions;

public class UserNotLoggedInException extends Exception {
    public UserNotLoggedInException() {
    }

    public UserNotLoggedInException(String message) {
        super(message);
    }
}
