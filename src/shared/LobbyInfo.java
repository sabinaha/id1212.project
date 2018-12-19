package shared;

import java.io.Serializable;
import java.util.ArrayList;

public class LobbyInfo implements Serializable {
    private final ArrayList<String> usersInLobby;

    public LobbyInfo(ArrayList<String> usersInLobby) {
        this.usersInLobby = new ArrayList<>(usersInLobby);
    }

    public ArrayList<String> getUsersInLobby() {
        return usersInLobby;
    }
}
