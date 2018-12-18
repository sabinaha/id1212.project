package shared;

import java.io.Serializable;

/**
 * Stores the user credentials in a UserCredential object.
 */
public class UserCredential implements Serializable {

    private final String username;
    private final String password;

    /**
     * Creates a UserCredential with the specific username and password.
     */
    public UserCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}