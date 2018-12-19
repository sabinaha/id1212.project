package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LobbyInfo implements Serializable {
    private List<String> lobbyInfo;

    public LobbyInfo () {
        lobbyInfo = new ArrayList<String>();
    }


}
