package server.controller;

import server.exceptions.*;
import server.integration.DB;
import server.model.Lobby;
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
     * @throws UserNotLoggedInException If the user is not logged in
     */
    private void assertLoggedIn(Token token) throws RemoteException {
        if (token == null || !userManager.hasUser(token))
            throw new RemoteException("You must be logged in to do this.", new UserNotLoggedInException("Token was null"));
    }

    private void assertInLobby(Token token) throws RemoteException {
        if (userManager.getUserByToken(token).getLobby() == null)
            throw new RemoteException("You must be in a lobby to do this.", new UserNotInLobbyException());
    }

    private void assertInGame(Token token) throws RemoteException {
    }

    @Override
    public void createLobby(String lobbyName, Token token) throws RemoteException {
        assertLoggedIn(token);
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
        assertLoggedIn(token);
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
        assertLoggedIn(token);
        assertInLobby(token);
        User user = userManager.getUserByToken(token);
        if (user.getLobby() == null)
            userManager.getClientRef(token).receiveResponse(Response.LOBBY_USER_NOT_IN_LOBBY);
        else {
            lobbyManager.leaveLobby(user);
            userManager.getClientRef(token).receiveResponse(Response.LOBBY_LEAVE_SUCCESSFUL);
        }
    }

    @Override
    public void startGame(Token token) throws RemoteException {

    }

    @Override
    public ServerInfo listLobbies(Token token) throws RemoteException {
        User user = userManager.getUserByToken(token);
        Lobby lobby = lobbyManager.getLobby(user);
        ArrayList<String> lobbyNames = new ArrayList<>();
        for (User lobbyUser : lobby.getUserList()) {
            lobbyNames.add(lobbyUser.getUsername());
        }
        return new ServerInfo(lobbyNames);
    }

    @Override
    public LobbyInfo listPlayers(Token token) throws RemoteException {
        assertInLobby(token);
        User user = userManager.getUserByToken(token);
        if (user.getLobby() == null) {
            userManager.getClientRef(token).receiveResponse(Response.LOBBY_USER_NOT_IN_LOBBY);
            return null;
        }
        ArrayList<String> usernames = new ArrayList<>();
        for (User lobbyUser : lobbyManager.getLobby(user).getUserList()) {
            usernames.add(lobbyUser.getUsername());
        }
        return new LobbyInfo(usernames);
    }

    @Override
    public Token login(UserCredential uc, Client client) throws RemoteException {
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
        User user = userManager.getUserByToken(token);
        if (user.getLobby() != null)
            lobbyManager.leaveLobby(user);
        if (userManager.hasUser(token))
            userManager.logoutUser(user);
    }

    @Override
    public void register(UserCredential uc, Client client) throws RemoteException {
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
