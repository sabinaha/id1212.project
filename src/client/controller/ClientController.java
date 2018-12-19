package client.controller;

import server.controller.ServerController;
import shared.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientController extends UnicastRemoteObject implements Client {

    private Server server;
    private boolean connected = true;
    public Scanner sc = new Scanner(System.in);
    volatile private String me;
    private String PROMPT = ">> ";
    private Token token;

    public ClientController() throws RemoteException {
    }

    /**
     * Will start the connection to the server and hold the connection running.
     */
    public void start(){
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            server = (Server) registry.lookup(ServerController.SERVER_REGISTRY_NAMESPACE);
            System.out.println("Rock-paper-scissors");

            // Program loop
            while(connected){
                showPrompt();
                String cmd = sc.nextLine();
                parseInput(cmd);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void showPrompt() {
        System.out.print(PROMPT);
    }

    /**
     * Client will send different prompts to the server depending on the input
     * @param cmd the command that the user wants to execute
     */
    public void parseInput (String cmd) {
        UserCredential uc = null;
        try {
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
                    server.createLobby(lobbyName, token);
                    break;
                case "join":
                    System.out.println("Please state which lobby you want to join down below");
                    String lobby = lobbyInfo();
                    server.joinLobby(lobby, token);
                    gameState();
                    break;
                case "register":
                    uc = userInfo();
                    server.register(uc, this);
                    break;
                case "players":
                    LobbyInfo players = server.listPlayers(token);
                    for (String s : players.getUsersInLobby()) {
                        System.out.println(s);
                    }
                    break;
                case "lobbies":
                    server.listLobbies(token);
                    break;
                case "quit":
                    server.quit(this.token);
                    this.me = null;
                    this.token = null;
                    break;
                case "help":
                    displayHelp();
                    break;
                default:
                    System.out.println("That is not a recognized command!");
                    displayHelp();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void gameState() throws RemoteException {
        boolean inGame = true;
        while (inGame) {
            showPrompt();
            String cmd = sc.nextLine();
            parseGameCmd(cmd);
        }
    }

    private void parseGameCmd(String cmd) throws RemoteException {
        switch (cmd) {
            case "start":
                server.startGame(token);
                break;
            case "rock":
                server.choose(Weapon.ROCK, token);
                break;
            case "paper":
                server.choose(Weapon.PAPER, token);
                break;
            case "scissors":
                server.choose(Weapon.SCISSORS, token);
                break;
            case "leave":
                server.leaveLobby(token);
                break;
            case "quit":
                server.quit(this.token);
                this.me = null;
                this.token = null;
                break;
            case "rules":
                System.out.println("==== RULES ====");
                displayRules();
                break;
            default:
                System.out.println("That is not a recognized command");
                displayGameHelp();

        }
    }

    /**
     * Display the commands that the user can write in the command line.
     * @return returns the help string to the user.
     */
    private void displayHelp(){
        System.out.println("==== HELP ====\nHere are the commands you may write:\n 'login' to login\n" +
                "'create' to create lobby\n'join' to join a lobby\n'register' to register a new user\n" +
                "'players' to list all players in a lobby\n 'lobbies' to list all lobbies\n" +
                "'quit' to quit the game");
    }

    private void displayGameHelp(){
        System.out.println("==== GAME HELP ====\nHere are the commands you may write:\n 'start' to start game\n" +
                "'rock' to choose rock as your weapon\n'paper' to choose paper as your weapon\n" +
                "'scissors' to choose scissors as your weapon\n" +
                "'quit' to quit the game\n'rules' to show the rules for the game");
    }

    private void displayRules() {
        System.out.println("When the game starts, please select rock, paper or scissors by writing 'rock', 'paper', 'scissors\n" +
                "You will receive one point for every player you beat. If everyone picks the same 'weapon', the score" +
                "will be 0\nHave fun!");
    }

    /**
     * When the user wants to add their user credentials this method is called.
     * @return Returns the user credential as an object.
     */
    private UserCredential userInfo() {
        System.out.print("Username: ");
        String usr = sc.nextLine();
        System.out.print("Password: ");
        String pw = sc.nextLine();

        return new UserCredential(usr, pw);
    }

    /**
     * When the user wants to add lobby information, this method is called.
     * @return returns the string of which holds the lobby name.
     */
    private String lobbyInfo() {
        System.out.print("Lobby name: ");
        String lobby = sc.nextLine();
        return lobby;
    }

    @Override
    public void displayInfo(Object object) {

    }

    @Override
    public void receiveResponse(Response response) {
        System.out.println("[DEV]: " + response);
    }
}
