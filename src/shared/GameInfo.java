package shared;

import java.io.Serializable;

public class GameInfo implements Serializable {
    public enum State {WON, LOST, DRAW}

    private int roundScore;
    private int totalScore;
    private int round;
    private int ofRounds;
    private State state;

    public GameInfo (int roundScore, int totalScore, int round, int ofRounds, State state) {
        this.roundScore = roundScore;
        this.totalScore = totalScore;
        this.round = round;
        this.ofRounds = ofRounds;
        this.state = state;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(int roundScore) {
        this.roundScore = roundScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getRound() {
        return round;
    }

    public int getOfRounds() {
        return ofRounds;
    }

    public State getState() {
        return state;
    }
}
