package server.model;

import shared.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private Lobby lobby;
    private Map<User, Integer> userRoundPoints = new ConcurrentHashMap();
    private Map<User, Integer> userTotalPoints = new ConcurrentHashMap<>();
    private Map<User, Weapon> moveList = new ConcurrentHashMap<>();
    private int roundsToPlay;
    private int roundsPlayed;

    private final static int ROUNDS = 2;

    public Game(Lobby lobby) {
        this.lobby = lobby;
        this.roundsPlayed = 0;
        this.roundsToPlay = ROUNDS;
        for (User user : lobby.getUserList()) {
            userRoundPoints.put(user, 0);
            userTotalPoints.put(user, 0);
        }
    }

    public void makeMove(User user, Weapon weapon) {
        moveList.put(user, weapon);
        synchronized (this) {
            if (moveList.size() == userTotalPoints.size()) {
                rewardPlayers();
                roundsPlayed++;
            }
        }
    }

    public HashMap<User, Integer> determineWinners() {
        int max = findHighestScore();
        HashMap<User,Integer> winners = new HashMap<>();
        for (Map.Entry<User, Integer> entry : userTotalPoints.entrySet()){
            if (entry.getValue() == max){
                winners.put(entry.getKey(), entry.getValue());
            }
        }
        return winners;
    }

    private int findHighestScore() {
        int max = 0;
        for (Map.Entry<User, Integer> entry : userTotalPoints.entrySet()){
            if (entry.getValue() > max){
                max = entry.getValue();
            }
        }
        return max;
    }

    public void leaveGame(User user) {
        userRoundPoints.remove(user);
        userTotalPoints.remove(user);
        moveList.remove(user);
    }

    private void rewardPlayers(){
        for (User user : lobby.getUserList()){ //Reset round rewards
            userRoundPoints.put(user, 0);
        }
        for (User user : moveList.keySet()) {
            int points = 0;
            Weapon w = moveList.get(user);
            for (Weapon weapon : moveList.values()){
                switch (w){
                    case ROCK:
                        switch (weapon){
                            case PAPER:
                                break;
                            case SCISSORS:
                                points++;
                                break;
                        }
                        break;
                    case SCISSORS:
                        switch (weapon){
                            case ROCK:
                                break;
                            case PAPER:
                                points++;
                                break;
                        }
                        break;
                    case PAPER:
                        switch (weapon){
                            case SCISSORS:
                                break;
                            case ROCK:
                                points++;
                                break;
                        }
                        break;
                }
            }
            givePoints(user, points);
        }
        moveList.clear();
    }

    private void givePoints(User user, int points){
        userRoundPoints.put(user, points);
        int previousPoints = userTotalPoints.get(user);
        userTotalPoints.put(user, (previousPoints + points));
    }

    public ArrayList<User> userWhoMadeTheirMoves() {
        return new ArrayList<>(moveList.keySet());
    }

    boolean isGameFinished() {
        return roundsPlayed == roundsToPlay;
    }


}
