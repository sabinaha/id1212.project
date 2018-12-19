package server.model;

import server.exceptions.GameOngoingException;
import server.exceptions.LobbyAlreadyExistsException;
import server.exceptions.LobbyDontExistException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LobbyManager {

    Map<String, Lobby> lobbies = new HashMap<>();

    synchronized public void createNewLobby(String name, User creator) throws LobbyAlreadyExistsException {
        if (lobbies.containsKey(name))
            throw new LobbyAlreadyExistsException();
        lobbies.put(name, new Lobby(name, creator));
        creator.setLobby(name);
    }

    synchronized public void deleteLobby(String name) {
        lobbies.remove(name);
    }

    synchronized public ArrayList<String > getAllLobbies() {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Lobby> entry : lobbies.entrySet()) {
            list.add(entry.getValue().getName());
        }
        return list;
    }

    synchronized void leaveLobby(User user) {
        lobbies.get(user.getLobby()).removeUser(user);
        user.setLobby(null);
    }

    synchronized public void joinLobby(String lobby, User user) throws LobbyDontExistException, GameOngoingException {
        if (!lobbies.containsKey(lobby))
            throw new LobbyDontExistException();
        lobbies.get(lobby).addUser(user);
        user.setLobby(lobby);
    }

    synchronized public Lobby getLobby(User user) {
        return lobbies.get(user.getLobby());
    }
}
