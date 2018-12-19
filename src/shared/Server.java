package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    void createLobby(String lobbyName) throws RemoteException;
    void joinLobby(String lobbyName) throws RemoteException;
    void leaveLobby() throws RemoteException;
    void startGame() throws RemoteException;
    void listLobbies() throws RemoteException;
    void listPlayers() throws RemoteException;
    Token login(UserCredential uc, Client client) throws RemoteException;
    void quit(Token token) throws RemoteException;
    void register(UserCredential uc, Client client) throws RemoteException;
    void choose (Weapon weapon) throws RemoteException;
}
