package server.controller;

import shared.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerController extends UnicastRemoteObject implements Server {

    public static String SERVER_REGISTRY_NAMESPACE = "SNELL_SERVER";

    public ServerController() throws RemoteException {
        System.out.println("Starting server");
    }

    @Override
    public void createLobby(String lobbyName) {

    }

    @Override
    public void joinLobby(String lobbyName) {

    }

    @Override
    public void leaveLobby() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void listLobbies() {

    }

    @Override
    public void listPlayers() {

    }

    @Override
    public Token login(UserCredential uc, Client client) {
        return null;
    }

    @Override
    public void quit(Token token) {

    }

    @Override
    public void register(UserCredential uc, Client client) {

    }

    @Override
    public void choose(Weapon weapon) {

    }
}
