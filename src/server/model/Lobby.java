package server.model;

import java.util.ArrayList;

public class Lobby {

    private final String name;
    private final ArrayList<User> userList = new ArrayList<>();
    private final User owner;

    /**
     * Creates a Lobby, with the user who created the lobby.
     * @param name The name of the lobby to craete.
     * @param user The user who created it.
     */
    public Lobby(String name, User user) {
        this.name = name;
        userList.add(user);
        owner = user;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void removeUser(User user) {
        userList.remove(user);
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "name='" + name + '\'' +
                ", userList=" + userList +
                ", owner=" + owner +
                '}';
    }
}
