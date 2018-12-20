package server.model;

import shared.Weapon;

import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    private final HashMap<Lobby, Game> games;

    public GameManager() {
        this.games = new HashMap<>();
    }

    synchronized public void startGame(Lobby lobby) {
        games.put(lobby, new Game(lobby));
    }

    synchronized public Game getGame(Lobby lobby) {
        return games.get(lobby);
    }

    public void makeMove(User user, Lobby lobby, Weapon move) {
        Game game;
        synchronized (this) {
            game = games.get(lobby);
        }
        game.makeMove(user, move);
    }

    synchronized public boolean gameIsOver(Lobby lobby) {
        return games.get(lobby).isGameFinished();
    }

    synchronized public ArrayList<User> userWhoMadeTheirMoves(Lobby lobby) {
        return games.get(lobby).userWhoMadeTheirMoves();
    }

    synchronized public HashMap<User, Integer> determineWinners(Lobby lobby) {
        return games.get(lobby).determineWinners();
    }
}
