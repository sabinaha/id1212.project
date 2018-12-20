package client.controller;

import server.controller.ServerController;
import server.exceptions.UserNotLoggedInException;
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
    private String PROMPT = ">> ";
    private Token token;
    private boolean inGame;

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

    /**
     * This method will print the prompt.
     */
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
                    break;
                case "register":
                    uc = userInfo();
                    server.register(uc, this);
                    break;
                case "lobbies":
                    server.listLobbies(token);
                    break;
                case "quit":
                    server.quit(this.token);
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
            if (e.getCause().getCause() instanceof UserNotLoggedInException){
                System.out.println("--- You have to be logged in, in order to do this. ---");
            }
            else {
                e.printStackTrace();
            }
        }
    }

    /**
     * If the user is in a lobby, they will not be able to write specific commands like "login". This method checks
     * to see if the user is in a lobby.
     * @throws RemoteException
     */
    private void gameState() throws RemoteException {
        inGame = true;
        while (inGame) {
            showPrompt();
            String cmd = sc.nextLine();
            parseGameCmd(cmd);
        }
    }

    /**
     * The commands which the user can write when in a lobby/game will be parsed here and sent to the corresponding
     * method in the server.
     * @param cmd The command which the user has written.
     */
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
                inGame = false;
                break;
            case "quit":
                server.quit(this.token);
                this.token = null;
                break;
            case "rules":
                System.out.println("==== RULES ====");
                displayRules();
                break;
            case "players":
                LobbyInfo players = server.listPlayers(token);
                if (players == null){
                    return;
                }
                for (String s : players.getUsersInLobby()) {
                    System.out.println(s);
                }
                break;
            default:
                System.out.println("That is not a recognized command");
                displayGameHelp();
        }
    }

    /**
     * Display the commands that the user can write in the command line.
     */
    private void displayHelp(){
        System.out.println("==== HELP ====\nHere are the commands you may write:\n 'login' to login\n" +
                "'create' to create lobby\n'join' to join a lobby\n'register' to register a new user\n" +
                "'players' to list all players in a lobby\n 'lobbies' to list all lobbies\n" +
                "'quit' to quit the game");
    }

    /**
     * The method will be called when the user wants help when in a lobby/game.
     */
    private void displayGameHelp(){
        System.out.println("==== GAME HELP ====\nHere are the commands you may write:\n 'start' to start game\n" +
                "'rock' to choose rock as your weapon\n'paper' to choose paper as your weapon\n" +
                "'scissors' to choose scissors as your weapon\n" +
                "'quit' to quit the game\n'rules' to show the rules for the game");
    }

    /**
     * The method will be called when the user wants to see the game rules.
     */
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

    /**
     * The method which the server will send feedback to the client.
     * @param response The response from the server.
     */
    @Override
    public void receiveResponse(Response response) {
        String s = "";
        try {
            switch (response) {
                case LOGIN_SUCCESSFUL:
                    s = "Login successful!";
                    break;
                case LOBBY_CREATE_SUCCESS:
                    s = "Lobby was successfully created!";
                    gameState();
                    break;
                case LOBBY_ALREADY_EXISTS:
                    s = "Lobby already exists, try joining it or create a new one.";
                    break;
                case LOBBY_JOIN_SUCCESS:
                    s = "Joining the lobby was successful!";
                    gameState();
                    break;
                case LOBBY_JOIN_FAILED:
                    s = "Could not connect to the lobby";
                    break;
                case LOBBY_DONT_EXISTS:
                    s = "That lobby does not exists";
                    break;
                case LOBBY_USER_NOT_IN_LOBBY:
                    s = "There are no players in the lobby.";
                default:
                    s = response.toString();
            }
            System.out.println(">> " + s + " <<");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
