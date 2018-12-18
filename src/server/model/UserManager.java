package server.model;

import shared.Client;
import shared.Token;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages logged in users. A user that is in this
 */
public class UserManager {

    Map<Long, User> players = new ConcurrentHashMap();
    private Object idLock = null;
    private static int playerID = 0;

    public Token addUser(String newUser, Client clientRef) {
        int id;
        synchronized (idLock) {
            id = playerID++;
        }
        Token token = new Token(id);
        User user = new User(newUser, token, clientRef);
        players.put(token.getId(), user);
        return token;
    }
}
