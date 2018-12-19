package server.model;

import shared.Client;
import shared.Token;

public class User {

    private final String username;
    private final Token token;
    private final Client clientRef;
    private String inLobbyName = null;

    public User(String username, Token token, Client clientRef) {
        this.username = username;
        this.token = token;
        this.clientRef = clientRef;
    }

    public String getUsername() {
        return username;
    }

    public Token getToken() {
        return token;
    }

    public Client getClientRef() {
        return clientRef;
    }

    public void setLobby(String lobby) {
        inLobbyName = lobby;
    }

    public String getLobby() {
        return inLobbyName;
    }
}
