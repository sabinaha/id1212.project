package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Object implements Serializable {
    private List<String> lobbyInfo;

    public Object() {
        lobbyInfo = new ArrayList<String>();
    }


}
