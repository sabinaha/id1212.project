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

//    public synchronized ArrayList<String > getAllLobbies() {
//        ArrayList<String> list = new ArrayList<>();
//        for (Map.Entry<String, Lobby> entry : lobbies.entrySet()) {
//            list.add(entry.getValue().getName());
//        }
//        return list;
//    }

    public synchronized void leaveLobby(User user) {
        if (!lobbies.containsKey(user.getLobby()))
            return;
        Lobby lobby = lobbies.get(user.getLobby());
        lobby.removeUser(user);
        if (lobby.getUserList().size() == 0) {
            lobbies.remove(user.getLobby());
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
        return lobbies.get(user.getLobby());
    }

    public synchronized ArrayList<String> getLobbyNames() {
        return new ArrayList<>(lobbies.keySet());
    }
}
