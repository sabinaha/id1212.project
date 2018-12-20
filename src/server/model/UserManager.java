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
    private Object idLock = new Object();
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

    synchronized public boolean hasUser(Token token) {
        return players.containsKey(token.getId());
    }

    synchronized public User getUserByToken(Token token) {
        return players.get(token.getId());
    }

    synchronized public Client getClientRef(Token token) {
        return players.get(token.getId()).getClientRef();
    }

    synchronized public void logoutUser(User user) {
        players.remove(user);
    }
}
