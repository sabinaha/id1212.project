package server.model;

import server.exceptions.LobbyAlreadyExistsException;
import server.exceptions.LobbyDontExistException;

import java.util.*;

public class LobbyManager {

    Map<String, Lobby> lobbies = new HashMap<>();

    public synchronized void createNewLobby(String name, User creator) throws LobbyAlreadyExistsException {
        if (lobbies.containsKey(name))
            throw new LobbyAlreadyExistsException();
        lobbies.put(name, new Lobby(name, creator));
        creator.setLobby(name);
    }

    public synchronized void leaveLobby(User user) {
        if (!lobbies.containsKey(user.getLobbyName()))
            return;
        Lobby lobby = lobbies.get(user.getLobbyName());
        lobby.removeUser(user);
        if (lobby.getUserList().size() == 0) {
            lobbies.remove(user.getLobbyName());
        }
        user.setLobby(null);
    }

    public synchronized void joinLobby(String lobby, User user) throws LobbyDontExistException {
        if (!lobbies.containsKey(lobby))
            throw new LobbyDontExistException();
        lobbies.get(lobby).addUser(user);
        user.setLobby(lobby);
    }

    public synchronized Lobby getLobby(User user) {
        return lobbies.get(user.getLobbyName());
    }

    public synchronized Lobby getLobbyByName(String lobbyName) {
        return lobbies.get(lobbyName);
    }

    public synchronized ArrayList<String> getLobbyNames() {
        return new ArrayList<>(lobbies.keySet());
    }
}
