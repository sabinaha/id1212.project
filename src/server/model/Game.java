package server.model;

import shared.UserCredential;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    Map<User, Integer> playerScores = new ConcurrentHashMap();
    public Game(UserCredential user){
        
    }
}
