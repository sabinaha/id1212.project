package server.model;

import shared.GameInfo;
import shared.Weapon;

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

    public synchronized void makeMove(User user, Lobby lobby, Weapon move) {
        games.get(lobby).makeMove(user, move);
    }

    synchronized public GameInfo getGameState(Lobby lobby, User user) {
        return games.get(lobby).getGameState(user);
    }

    synchronized public boolean gameIsOver(Lobby lobby) {
        return games.get(lobby).isGameFinished();
    }

    synchronized public boolean isStartOfRound(Lobby lobby) {
        return games.get(lobby).userWhoMadeTheirMoves().size() == 0;
    }

    public synchronized void leaveGame(User user, Lobby lobby) {
        games.get(lobby).leaveGame(user);
    }
}
