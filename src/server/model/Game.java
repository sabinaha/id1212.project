package server.model;

import shared.Weapon;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    Lobby lobby;
    Map<User, Integer> userRoundPoints = new ConcurrentHashMap();
    Map<User, Integer> userTotalPoints = new ConcurrentHashMap<>();
    Map<User, Weapon> moveList = new ConcurrentHashMap<>();
    int roundsToPlay;
    int roundsPlayed;

    public Game(Lobby lobby, int rounds){
        this.lobby = lobby;
        this.roundsPlayed = 0;
        this.roundsToPlay = 5;
        for (User user : lobby.getUserList()) {
            userRoundPoints.put(user, 0);
            userTotalPoints.put(user, 0);
        }
    }

    public void makeMove(User user, Weapon weapon){
        if (moveList.size() < userRoundPoints.size()){
            moveList.put(user, weapon);
        }else{
            System.out.println("Vad i helvete håller du på med? Vi är ju redan klara! Det här är ju livsfarligt! JAG STARTAR!");
            start();
        }
    }

    public void start() {
        if (roundsPlayed < roundsToPlay){
            rewardPlayers();
            roundsPlayed++;
        }else if (roundsPlayed == roundsToPlay){
            HashMap<User, Integer> winners = determineWinners();
            for (Map.Entry<User, Integer> entry : winners.entrySet()){
                System.out.println("Winner: " + entry.getKey().getUsername() + " with score: " + entry.getValue());
            }
        }else{
            System.out.println("rip");
        }


    }

    private HashMap<User, Integer> determineWinners() {
        int max = findHighestScore();
        System.out.println(max);
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
            System.out.println("User: " + user.getUsername() + " receives " + points + " for a total of " + (userTotalPoints.get(user) + points) + " points!");
            givePoints(user, points);
        }

    }

    private void givePoints(User user, int points){
        userRoundPoints.put(user, points);
        int previousPoints = userTotalPoints.get(user);
        userTotalPoints.put(user, (previousPoints + points));

    }


}
