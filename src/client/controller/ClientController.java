package client.controller;

import server.controller.ServerController;
import server.exceptions.UserNotInGameException;
import server.exceptions.UserNotInLobbyException;
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
     * Will playRound the connection to the server and hold the connection running.
     */
    public void start(){
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            server = (Server) registry.lookup(ServerController.SERVER_REGISTRY_NAMESPACE);
            System.out.println("Rock-Paper-Scissors");

            // Program loop
            while(connected){
                showPrompt();
                String cmd = sc.nextLine();
                parseCmd(cmd);
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
    public void parseCmd(String cmd) {
        UserCredential uc = null;
        try {
            switch (cmd) {
                case "login":
                    uc = userInput();
                    token = server.login(uc, this);
                    break;
                case "create":
                    System.out.println("Please fill out the name of the lobby down below.");
                    String lobbyName = lobbyInput();
                    server.createLobby(lobbyName, token);
                    break;
                case "join":
                    listAllLobbies();
                    System.out.println("Please state which lobby you want to join down below");
                    String lobby = lobbyInput();
                    server.joinLobby(lobby, token);
                    listPlayersInLobby();
                    break;
                case "register":
                    uc = userInput();
                    server.register(uc, this);
                    break;
                case "lobbies":
                    listAllLobbies();
                    break;
                case "help":
                    displayHelp();
                    break;
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
                    this.token = null;
                    connected = false;
                    break;
                case "rules":
                    System.out.println("==== RULES ====");
                    displayRules();
                    break;
                case "players":
                    listPlayersInLobby();
                    break;
                case "a":
                    UserCredential a = new UserCredential("a", "a");
                    token = server.login(a, this);
                    server.createLobby("banana", token);
                    break;
                case "b":
                    UserCredential b = new UserCredential("b", "b");
                    token = server.login(b, this);
                    server.joinLobby("banana", token);
                    break;
                default:
                    System.out.println("That is not a recognized command!");
                    displayHelp();
            }
        } catch (RemoteException e) {
            if (e.getCause().getCause() instanceof UserNotLoggedInException){
                System.out.println(">> You have to be logged in, in order to do this. <<");
            } else if (e.getCause().getCause() instanceof UserNotInLobbyException) {
                System.out.println(">> You have to be in a lobby to do that! <<");
            } else if (e.getCause().getCause() instanceof UserNotInGameException) {
                System.out.println(">> You have to be in a game to do that <<");
            }
            else {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method lists all the current lobbies.
     */
    private void listAllLobbies() throws RemoteException {
        ServerInfo lobbies = server.listLobbies(token);
        if (lobbies.getLobbyNames().size() == 0) {
            System.out.println("No current lobbies");
            return;
        }
        System.out.println("CURRENT LOBBIES");
        for (String l : lobbies.getLobbyNames()) {
            System.out.println("• " + l);
        }
    }

    /**
     * This method lists all of the players in a lobby.
     */
    private void listPlayersInLobby() throws RemoteException {
        LobbyInfo players = server.listPlayers(token);
        if (players == null){
            return;
        }
        System.out.println("PLAYERS IN LOBBY");
        for (String s : players.getUsersInLobby()) {
            System.out.println("• " + s);
        }
    }

    /**
     * This method will display the commands that the user can write in the command line.
     */
    private void displayHelp(){
        System.out.println("==== HELP ====\nHere are the commands you may write:\n'login' to login\n" +
                "'create' to create lobby\n'join' to join a lobby\n'register' to register a new user\n" +
                "'players' to list all players in a lobby\n'lobbies' to list all lobbies\n" +
                "'quit' to quit the game\n'playRound' to playRound game" +
                "\n'rock' to choose rock as your weapon\n'paper' to choose paper as your weapon" +
                "\n'scissors' to choose scissors as your weapon" +
                "\n'rules' to show the rules for the game");
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
    private UserCredential userInput() {
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
    private String lobbyInput() {
        System.out.print("Lobby name: ");
        String lobby = sc.nextLine();
        return lobby;
    }

    /**
     * Displaying info from the server which can be different objects depending on what kind of message to
     * display to the client.
     * @param object the object which is sent to the client.
     */
    @Override
    public void displayInfo(Object object) {
        if (object instanceof GameInfo) {
            String s = "";
            GameInfo gameInfo = (GameInfo) object;
            if (!gameInfo.gameIsDone()) {
                switch (gameInfo.getState()) {
                    case WON:
                        s = "You won the round!";
                        break;
                    case LOST:
                        s = "You lost the round!";
                        break;
                    case DRAW:
                        s = "The round was a draw!";
                        break;
                }
            } else {
                switch (gameInfo.getFinalResult()) {
                    case WON:
                        s = "You won the game!";
                        break;
                    case LOST:
                        s = "You lost the game!";
                        break;
                    case DRAW:
                        s = "The game was a draw!";
                        break;
                }
            }

            String info = printGameInfo(gameInfo);
            System.out.println(info + s);
        }
    }

    private String printGameInfo(GameInfo gameInfo) {
        int roundScore = gameInfo.getRoundScore();
        int totalScore = gameInfo.getTotalScore();
        int currentRound = gameInfo.getRound() + 1;
        int totalRounds = gameInfo.getOfRounds();

        String s;
        if (gameInfo.gameIsDone()) {
            s = currentRound + " out of " + totalRounds + "\nTotal score: " + totalScore + "\n";
        } else {
             s = currentRound + " out of " + totalRounds + "\nRound score: " +
                     roundScore + "\nTotal score: " + totalScore + "\n";
        }

        return s;
    }

    /**
     * The method which the server will send feedback to the client. The client can then print different feedback
     * messages to the user depending on the response from the server.
     * @param response The response from the server.
     */
    @Override
    public void receiveResponse(Response response) {
        String s = "";
        switch (response) {
            case LOGIN_SUCCESSFUL:
                s = "Login successful!";
                break;
            case LOGIN_INCORRECT_CRED:
                s = "The username or password was wrong. Try again!";
                break;
            case LOBBY_CREATE_SUCCESS:
                s = "Lobby was successfully created!";
                break;
            case LOBBY_ALREADY_EXISTS:
                s = "Lobby already exists, try joining it or create a new one.";
                break;
            case LOBBY_JOIN_SUCCESS:
                s = "Joining the lobby was successful!";
                break;
            case LOBBY_JOIN_FAILED:
                s = "Could not connect to the lobby";
                break;
            case LOBBY_DONT_EXISTS:
                s = "That lobby does not exists";
                break;
            case LOBBY_USER_NOT_IN_LOBBY:
                s = "There are no players in the lobby.";
                break;
            case LOBBY_LEAVE_SUCCESSFUL:
                s = "Leaving lobby was successful!";
                break;
            case LOBBY_GAME_ONGOING_ERROR:
                s = "A game is already ongoing, you can't join at the moment. Try again later!";
                break;
            case REG_DUPL_USERNAME:
                s = "That username already exists!";
                break;
            case REG_SUCCESSFUL:
                s = "Registration was successful!";
                break;
            case GAME_PROMPT_ACTION:
                s = "Please select 'rock', 'paper' or 'scissors'";
                break;
            case GAME_DONE:
                s = "Game is done!";
                break;
            case GAME_STARTED:
                s = "Game has started!";
                break;
            case GAME_NO_ONGOING_GAME:
                s = "There is no ongoing game at the moment.";
                break;
            default:
                s = response.toString();
        }
            System.out.println(">> " + s + " <<");
    }
}
