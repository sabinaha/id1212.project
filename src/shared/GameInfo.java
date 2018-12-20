package shared;

import java.io.Serializable;

public class GameInfo implements Serializable {
    public enum State {WON, LOST, DRAW}

    private final int roundScore;
    private final int totalScore;
    private final int round;
    private final int ofRounds;
    private final State state;
    private final boolean gameIsDone;
    private final State finalResult;

    public GameInfo (int roundScore, int totalScore, int round, int ofRounds, State state, boolean gameIsDone, State finalResult) {
        this.roundScore = roundScore;
        this.totalScore = totalScore;
        this.round = round;
        this.ofRounds = ofRounds;
        this.state = state;
        this.gameIsDone = gameIsDone;
        this.finalResult = finalResult;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public int getTotalScore() {
        return totalScore;
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

    public boolean isGameIsDone() {
        return gameIsDone;
    }

    public State getFinalResult() {
        return finalResult;
    }

    @Override
    public String toString() {
        return String.format("Round: %d of %d | Score: %d, Total: %d | State:", round,  ofRounds, roundScore, totalScore, state);
    }
}
