package server.controller;

import server.exceptions.IncorrectCredentialsException;
import server.exceptions.UserAlreadyExistsException;
import server.integration.DB;
import server.model.User;
import server.model.UserManager;
import shared.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerController extends UnicastRemoteObject implements Server {

    public static String SERVER_REGISTRY_NAMESPACE = "SNELL_SERVER";
    private UserManager userManager = new UserManager();

    public ServerController() throws RemoteException {
        System.out.println("Starting server");
    }

    @Override
    public void createLobby(String lobbyName) throws RemoteException {

    }

    @Override
    public void joinLobby(String lobbyName) throws RemoteException {

    }

    @Override
    public void leaveLobby() throws RemoteException {

    }

    @Override
    public void startGame() throws RemoteException {

    }

    @Override
    public void listLobbies() throws RemoteException {

    }

    @Override
    public void listPlayers() throws RemoteException {

    }

    @Override
    public Token login(UserCredential uc, Client client) throws RemoteException {
        System.out.println("[SERVER] login");
        Token token = null;
        try {
            DB.getDB().login(uc);
            token = userManager.addUser(uc.getUsername(), client);
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            client.receiveResponse(Response.LOGIN_INCORRECT_CRED);
            return null;
        }
        client.receiveResponse(Response.LOGIN_SUCCESSFUL);
        return token;
    }

    @Override
    public void quit(Token token) {

    }

    @Override
    public void register(UserCredential uc, Client client) throws RemoteException {
        System.out.println("[SERVER] register");
        try {
            DB.getDB().registerUser(uc);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
            client.receiveResponse(Response.REG_DUPL_USERNAME);
        }
        client.receiveResponse(Response.REG_SUCCESSFUL);
    }

    @Override
    public void choose(Weapon weapon) throws RemoteException {

    }
}
