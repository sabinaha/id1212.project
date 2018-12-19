package shared;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerInfo implements Serializable {

    private final ArrayList<String> lobbyNames;

    public ServerInfo(ArrayList<String> lobbyNames) {
        this.lobbyNames = new ArrayList<>(lobbyNames);
    }

    public ArrayList<String> getLobbyNames() {
        return lobbyNames;
    }
}
