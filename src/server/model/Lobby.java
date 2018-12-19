package server.model;

import java.util.ArrayList;

public class Lobby {

    private String name;
    private ArrayList<User> userList = new ArrayList<>();

    /**
     * Creates a Lobby, with the user who created the lobby.
     * @param name
     * @param user
     */
    public Lobby(String name, User user) {
        this.name = name;
        userList.add(user);
    }

    public String getName() {
        return name;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }
}
