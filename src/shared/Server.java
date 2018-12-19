package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    void createLobby(String lobbyName, Token token) throws RemoteException;
    void joinLobby(String lobbyName, Token token) throws RemoteException;
    void leaveLobby(Token token) throws RemoteException;
    void startGame(Token token) throws RemoteException;
    ServerInfo listLobbies(Token token) throws RemoteException;
    LobbyInfo listPlayers(Token token) throws RemoteException;
    Token login(UserCredential uc, Client client) throws RemoteException;
    void quit(Token token) throws RemoteException;
    void register(UserCredential uc, Client client) throws RemoteException;
    void choose (Weapon weapon, Token token) throws RemoteException;
}
