package shared;

public interface Server {
    void createLobby(String lobbyName);
    void joinLobby(String lobbyName);
    void leaveLobby();
    void startGame();
    void listLobbies();
    void listPlayers();
    Token login(UserCredential uc, Client client);
    void quit(Token token);
    void register(UserCredential uc, Client client);
    void choose (Weapon weapon);
}