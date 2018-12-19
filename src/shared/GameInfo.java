package shared;

public class GameInfo {
    private int roundScore;
    private int totalScore;

    public GameInfo (int roundScore, int totalScore) {
        this.roundScore = roundScore;
        this.totalScore = totalScore;
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
}
