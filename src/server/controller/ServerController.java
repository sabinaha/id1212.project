package server.controller;

import server.exceptions.IncorrectCredentialsException;
import server.exceptions.LobbyAlreadyExistsException;
import server.exceptions.LobbyDontExistException;
import server.exceptions.UserAlreadyExistsException;
import server.integration.DB;
import server.model.LobbyManager;
import server.model.User;
import server.model.UserManager;
import shared.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerController extends UnicastRemoteObject implements Server {

    public static String SERVER_REGISTRY_NAMESPACE = "SNELL_SERVER";
    private UserManager userManager = new UserManager();
    private LobbyManager lobbyManager = new LobbyManager();

    public ServerController() throws RemoteException {
        System.out.println("Starting server");
    }

    @Override
    public void createLobby(String lobbyName, Token token) throws RemoteException {
        User user = userManager.getUserByToken(token);
        try {
            lobbyManager.createNewLobby(lobbyName, user);
        } catch (LobbyAlreadyExistsException e) {
            e.printStackTrace();
            userManager.getClientRef(token).receiveResponse(Response.LOBBY_ALREADY_EXISTS);
            return;
        }
        userManager.getClientRef(token).receiveResponse(Response.LOBBY_CREATE_SUCCESS);
    }

    @Override
    public void joinLobby(String lobbyName, Token token) throws RemoteException {
        User user = userManager.getUserByToken(token);
        try {
            lobbyManager.joinLobby(lobbyName, user);
        } catch (LobbyDontExistException e) {
            e.printStackTrace();
            userManager.getClientRef(token).receiveResponse(Response.LOBBY_JOIN_FAILED);
            return;
        }
        userManager.getClientRef(token).receiveResponse(Response.LOBBY_JOIN_SUCCESS);
    }

    @Override
    public void leaveLobby(Token token) throws RemoteException {

    }

    @Override
    public void startGame(Token token) throws RemoteException {

    }

    @Override
    public ServerInfo listLobbies(Token token) throws RemoteException {
        return null;
    }

    @Override
    public LobbyInfo listPlayers(Token token) throws RemoteException {
        // AUTH=???
        User user = userManager.getUserByToken(token);
        ArrayList<String> usernames = new ArrayList<>();
        for (User lobbyUser : lobbyManager.getLobby(user).getUserList()) {
            usernames.add(lobbyUser.getUsername());
        }
        return new LobbyInfo(usernames);
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
    public void choose(Weapon weapon, Token token) throws RemoteException {

    }
}
