package server.model;

import shared.Weapon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    Lobby lobby;
    Map<User, Integer> userRoundPoints = new ConcurrentHashMap();
    Map<User, Weapon> moveList = new ConcurrentHashMap<>();

    public Game(Lobby lobby){
        this.lobby = lobby;
        for (User user : lobby.getUserList()) {
            userRoundPoints.put(user, 0);
        }
    }

    public void move(User user, Weapon weapon){
        if (moveList.size() < userRoundPoints.size()){
            moveList.put(user, weapon);
        }else{
            System.out.println("Vad i helvete håller du på med? Vi är ju redan klara! Det här är ju livsfarligt!");
        }
    }

    private void RewardWinners(){
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

    }

    private void givePoints(User user, int points){
        userRoundPoints.put(user, points);
    }


}
