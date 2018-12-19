package client.controller;

import server.controller.ServerController;
import shared.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientController implements Client {

    private Server server;
    private boolean connected = true;
    public Scanner sc = new Scanner(System.in);
    volatile private String me;
    private String PROMPT = ">";
    private Token token;

    /**
     * Will start the connection to the server and hold the connection running.
     */
    public void start(){
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            server = (Server) registry.lookup(ServerController.SERVER_REGISTRY_NAMESPACE);
            System.out.println("Rock paper scissors");

            // Program loop
            while(connected){
                String cmd = sc.nextLine();
                parseInput(cmd);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayLobbyInfo(LobbyInfo lobbyInfo) {

    }

    public void displayGameInfo(GameInfo gameinfo) {

    }

    public void displayServerInfo(ServerInfo serverInfo) {

    }

    /**
     * Client will send different prompts to the server depending on the input
     * @param cmd the command that the user wants to execute
     */
    public void parseInput (String cmd) {
        UserCredential uc = null;
        switch (cmd) {
            case "login":
                uc = userInfo();
                token = server.login(uc, this);
                if (token != null)
                    this.me = uc.getUsername();
                break;
            case "create":
                System.out.println("Please fill out the name of the lobby down below.");
                String lobbyName = lobbyInfo();
                server.createLobby(lobbyName);
                break;
            case "join":
                System.out.println("Please state which lobby you want to join down below");
                String lobby = lobbyInfo();
                server.joinLobby(lobby);
                break;
            case "register":
                uc = userInfo();
                server.register(uc, this);
                break;
            case "players":
                break;
            case "lobbies":
                break;
            case "quit":
                if(!loggedIn(this.token))
                    return;
                server.quit(this.token);
                this.me = null;
                this.token = null;
                break;
            case "leave":
                break;
            case "help":
                String msg = displayHelp();
                System.out.println(msg);
                break;

        }
    }

    private String displayHelp(){
        String help = "Here are the commands you may write:\n 'login' to login\n" +
                "'create' to create lobby\n'join' to join a lobby\n'register' to register a new user\n" +
                "'players' to list all players in a lobby\n 'lobbies' to list all lobbies\n" +
                "'quit' to quit the game\n 'leave' to leave the lobby";
        return help;
    }

    private UserCredential userInfo() {
        System.out.print("Username: ");
        String usr = sc.nextLine();
        System.out.print("Password: ");
        String pw = sc.nextLine();

        return new UserCredential(usr, pw);
    }

    private String lobbyInfo() {
        System.out.print("Lobby name: ");
        String lobby = sc.nextLine();
        return lobby;
    }

    private boolean loggedIn(Token token) {
        boolean loggedIn;
        if (token == null) {
            System.out.println("You have to be logged in!");
            loggedIn = false;
            return loggedIn;
        }
        loggedIn = true;
        return loggedIn;
    }
}
