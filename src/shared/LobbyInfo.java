package shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class handles all the lobby information, which will be shown when the client wants to list all the players
 * in their current lobby.
 */
public class LobbyInfo implements Serializable {
    private final ArrayList<String> usersInLobby;

    public LobbyInfo(ArrayList<String> usersInLobby) {
        this.usersInLobby = new ArrayList<>(usersInLobby);
    }

    /**
     * The method will fetch a user in the clients current lobby.
     * @return A user in the current lobby.
     */
    public ArrayList<String> getUsersInLobby() {
        return usersInLobby;
    }
}
