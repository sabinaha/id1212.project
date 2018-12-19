package shared;

public interface Client {
    void displayLobbyInfo(LobbyInfo lobbyInfo);
    void displayGameInfo(GameInfo gameInfo);
    void displayServerInfo(ServerInfo serverInfo);
    void receiveResponse(Response response);
}
