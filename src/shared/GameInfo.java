package shared;

import java.io.Serializable;

public class GameInfo implements Serializable {
    public enum State {WON, LOST, DRAW}

    private int roundScore;
    private int totalScore;
    private int round;
    private int ofRounds;
    private State state;
    private boolean gameIsDone;

    public GameInfo (int roundScore, int totalScore, int round, int ofRounds, State state, boolean gameIsDone) {
        this.roundScore = roundScore;
        this.totalScore = totalScore;
        this.round = round;
        this.ofRounds = ofRounds;
        this.state = state;
        this.gameIsDone = gameIsDone;
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

    @Override
    public String toString() {
        return String.format("Round: %d of %d | Score: %d, Total: %d | State:", round,  ofRounds, roundScore, totalScore, state);
    }
}
