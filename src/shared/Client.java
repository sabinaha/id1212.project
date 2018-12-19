package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void displayLobbyInfo(LobbyInfo lobbyInfo) throws RemoteException;
    void displayGameInfo(GameInfo gameInfo) throws RemoteException;
    void displayServerInfo(ServerInfo serverInfo) throws RemoteException;
    void receiveResponse(Response response) throws RemoteException;
}
