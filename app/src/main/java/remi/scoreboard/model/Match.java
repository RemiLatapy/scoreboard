package remi.scoreboard.model;

import android.support.v4.util.Pair;

/**
 * Created by rlatapy on 05/06/2017.
 */

public class Match {

    private Player playerOne;
    private int scorePlayerOne;

    private Player playerTwo;
    private int scorePlayerTwo;

    public Match(Player playerOne, Player playerTwo)
    {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.scorePlayerOne = -1;
        this.scorePlayerTwo = -1;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public int getScorePlayerOne() {
        return scorePlayerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public int getScorePlayerTwo() {
        return scorePlayerTwo;
    }

    public void setScorePlayerOne(int scorePlayerOne) {
        this.scorePlayerOne = scorePlayerOne;
    }

    public void setScorePlayerTwo(int scorePlayerTwo) {
        this.scorePlayerTwo = scorePlayerTwo;
    }

    public boolean isFinished()
    {
        return scorePlayerOne >= 0 && scorePlayerTwo >= 0;
    }
}
