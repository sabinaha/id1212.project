package server.controller;

import server.exceptions.*;
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

    /**
     * Asserts that the user is logged in.
     * @param token The token to identify the user by.
     * @throws UserNotLoggedInException If the user is not loeed
     */
    private void assertLoggedIn(Token token) throws UserNotLoggedInException {
        if (token == null)
            throw new UserNotLoggedInException("Token was null");
        if (!userManager.hasUser(token))
            throw new UserNotLoggedInException("No such user is registred in userManager");
    }

    @Override
    public void createLobby(String lobbyName, Token token) throws RemoteException {
        try {
            assertLoggedIn(token);
        } catch (UserNotLoggedInException e) {
            System.out.println("throwing");
            throw new RemoteException("You must be logged in", e);
        }
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
            userManager.getClientRef(token).receiveResponse(Response.LOBBY_DONT_EXISTS);
            return;
        } catch (GameOngoingException e) {
            e.printStackTrace();
            userManager.getClientRef(token).receiveResponse(Response.LOBBY_GAME_ONGOING_ERROR);
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
