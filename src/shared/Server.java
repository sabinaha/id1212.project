package shared;

public interface Server {
    void createLobby(LobbyDTO lobbyDTO);
    void joinLobby(String lobbyName);
    void leaveLobby();
    void startGame();
    void listLobbies();
    void listPlayers();
    Token login(String username);
    void quit();
    void register(UserCredential uc, Client client);
}
