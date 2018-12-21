package server.model;

import shared.GameInfo;
import shared.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static shared.Weapon.*;

public class Game {
    private Lobby lobby;
    private Map<User, Integer> userRoundPoints = new HashMap<>();
    private Map<User, Integer> userTotalPoints = new HashMap<>();
    private Map<User, Weapon> userMoves = new HashMap<>();
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
        userMoves.put(user, weapon);
        synchronized (this) {
            if (userMoves.size() == userTotalPoints.size()) {
                calculateRound();
                roundsPlayed++;
                buildGameInfos();
                userMoves.clear();
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

    void leaveGame(User user) {
        userRoundPoints.remove(user);
        userTotalPoints.remove(user);
        userMoves.remove(user);
        gameStates.remove(user);
        if (userRoundPoints.size() == 1) {
            roundsPlayed = roundsToPlay - 1;
            buildGameInfos();
        }
    }

    private void calculateRound(){
        for (User user : lobby.getUserList()){ //Reset round rewards
            userRoundPoints.put(user, 0);
        }
        for (User user : userMoves.keySet()) {
            int points = 0;
            Weapon w = userMoves.get(user);
            for (Weapon weapon : userMoves.values()){
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
    }

    private void buildGameInfos() {
        for (User user : userRoundPoints.keySet()) {
            int roundScore, totalScore, round = roundsPlayed;
            roundScore = userRoundPoints.get(user);
            totalScore = userTotalPoints.get(user);
            GameInfo.State state = null;
            Weapon weapon = userMoves.get(user) != null ? userMoves.get(user) : NO_MOVE_YET;
            switch (weapon) {
                case ROCK:
                    if (userMoves.containsValue(Weapon.SCISSORS))
                        state = GameInfo.State.WON;
                    else if (userMoves.containsValue(Weapon.PAPER))
                        state = GameInfo.State.LOST;
                    else
                        state = GameInfo.State.DRAW;
                    break;
                case PAPER:
                    if (userMoves.containsValue(ROCK))
                        state = GameInfo.State.WON;
                    else if (userMoves.containsValue(Weapon.SCISSORS))
                        state = GameInfo.State.LOST;
                    else
                        state = GameInfo.State.DRAW;
                    break;
                case SCISSORS:
                    if (userMoves.containsValue(PAPER))
                        state = GameInfo.State.WON;
                    else if (userMoves.containsValue(ROCK))
                        state = GameInfo.State.LOST;
                    else
                        state = GameInfo.State.DRAW;
                    break;
            }
            GameInfo.State didIWin = determineWinners().containsKey(user) ? GameInfo.State.WON : GameInfo.State.LOST;
            didIWin = determineWinners().size() > 1 ? GameInfo.State.DRAW : didIWin;
            if (userTotalPoints.size() == 1) {
                didIWin = GameInfo.State.WON;
                state = GameInfo.State.WON;
            }
            gameStates.put(user, new GameInfo(roundScore, totalScore, round, ROUNDS, state, roundsPlayed == (ROUNDS - 1) , didIWin));
        }
    }

    private void givePoints(User user, int points){
        userRoundPoints.put(user, points);
        int previousPoints = userTotalPoints.get(user);
        userTotalPoints.put(user, (previousPoints + points));
    }

    ArrayList<User> userWhoMadeTheirMoves() {
        return new ArrayList<>(userMoves.keySet());
    }

    boolean isGameFinished() {
        return roundsPlayed + 1 == roundsToPlay;
    }


    GameInfo getGameState(User user) {
        return gameStates.get(user);
    }

    @Override
    public String toString() {
        return "Game{" +
                "lobby=" + lobby +
                ", userRoundPoints=" + userRoundPoints +
                ", userTotalPoints=" + userTotalPoints +
                ", userMoves=" + userMoves +
                ", gameStates=" + gameStates +
                ", roundsToPlay=" + roundsToPlay +
                ", roundsPlayed=" + roundsPlayed +
                '}';
    }
}
