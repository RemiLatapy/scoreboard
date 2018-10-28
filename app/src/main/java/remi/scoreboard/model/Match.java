package remi.scoreboard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rlatapy on 05/06/2017.
 */

public class Match implements Parcelable {

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };
    private Player playerOne;
    private int scorePlayerOne;
    private Player playerTwo;
    private int scorePlayerTwo;

    public Match(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.scorePlayerOne = -1;
        this.scorePlayerTwo = -1;
    }

    private Match(Parcel in) {
        playerOne = in.readParcelable(Player.class.getClassLoader());
        scorePlayerOne = in.readInt();
        playerTwo = in.readParcelable(Player.class.getClassLoader());
        scorePlayerTwo = in.readInt();
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public int getScorePlayerOne() {
        return scorePlayerOne;
    }

    public void setScorePlayerOne(int scorePlayerOne) {
        this.scorePlayerOne = scorePlayerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public int getScorePlayerTwo() {
        return scorePlayerTwo;
    }

    public void setScorePlayerTwo(int scorePlayerTwo) {
        this.scorePlayerTwo = scorePlayerTwo;
    }

    public boolean isFinished() {
        return scorePlayerOne >= 0 && scorePlayerTwo >= 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(playerOne, 0);
        dest.writeInt(scorePlayerOne);
        dest.writeParcelable(playerTwo, 0);
        dest.writeInt(scorePlayerTwo);
    }
}
