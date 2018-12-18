package server.exceptions;

public class UserNotLoggedInException extends Exception {
    public UserNotLoggedInException() {
        super("You can't log out if you're not logged in.");
    }
}
