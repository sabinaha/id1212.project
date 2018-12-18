package server.controller;

import shared.Server;

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
    public void login(String username) {

    }

    @Override
    public void quit() {

    }
}
