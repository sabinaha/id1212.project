package server.model;

import shared.GameInfo;
import shared.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static shared.Weapon.PAPER;
import static shared.Weapon.ROCK;

public class Game {
    private Lobby lobby;
    private Map<User, Integer> userRoundPoints = new HashMap<>();
    private Map<User, Integer> userTotalPoints = new HashMap<>();
    private Map<User, Weapon> moveList = new HashMap<>();
    private Map<User, GameInfo> gameStates = new HashMap<>();
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
                calculateRound();
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
        gameStates.remove(user);
    }

    private void calculateRound(){
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
        buildGameInfos();
        for (GameInfo value : gameStates.values()) {
            System.out.println(value);
        }
        moveList.clear();
    }

    private void buildGameInfos() {
        for (Map.Entry<User, GameInfo> entry : gameStates.entrySet()) {
            User user = entry.getKey();
            int roundScore, totalScore, round = roundsPlayed, ofRounds = ROUNDS;
            roundScore = userRoundPoints.get(user);
            totalScore = userTotalPoints.get(user);
            GameInfo.State state = null;
            Weapon weapon = moveList.get(user);
            switch (weapon) {
                case ROCK:
                    if (moveList.containsValue(Weapon.SCISSORS))
                        state = GameInfo.State.WON;
                    else if (moveList.containsValue(Weapon.PAPER))
                        state = GameInfo.State.LOST;
                    else
                        state = GameInfo.State.DRAW;
                    break;
                case PAPER:
                    if (moveList.containsValue(ROCK))
                        state = GameInfo.State.WON;
                    else if (moveList.containsValue(Weapon.SCISSORS))
                        state = GameInfo.State.LOST;
                    else
                        state = GameInfo.State.DRAW;
                    break;
                case SCISSORS:
                    if (moveList.containsValue(PAPER))
                        state = GameInfo.State.WON;
                    else if (moveList.containsValue(ROCK))
                        state = GameInfo.State.LOST;
                    else
                        state = GameInfo.State.DRAW;
                    break;
            }
            gameStates.put(user, new GameInfo(roundScore, totalScore, round, ofRounds, state));
        }
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


    public GameInfo getGameState(User user) {
        return gameStates.get(user);
    }

    @Override
    public String toString() {
        return "Game{" +
                "lobby=" + lobby +
                ", userRoundPoints=" + userRoundPoints +
                ", userTotalPoints=" + userTotalPoints +
                ", moveList=" + moveList +
                ", gameStates=" + gameStates +
                ", roundsToPlay=" + roundsToPlay +
                ", roundsPlayed=" + roundsPlayed +
                '}';
    }
}
