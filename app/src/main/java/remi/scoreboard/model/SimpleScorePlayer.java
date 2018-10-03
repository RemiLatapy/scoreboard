package remi.scoreboard.model;


import android.os.Parcel;
import android.os.Parcelable;

public class SimpleScorePlayer extends Player implements Parcelable {

    private int score;

    public SimpleScorePlayer(String name, int num) {
        super(name, num);
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public void addPoints(int points) {
        score += points;
    }

    ////// Parcelable implementation below
    private SimpleScorePlayer(Parcel in) {
        super(in);
        score = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SimpleScorePlayer> CREATOR = new Creator<SimpleScorePlayer>() {
        @Override
        public SimpleScorePlayer createFromParcel(Parcel in) {
            return new SimpleScorePlayer(in);
        }

        @Override
        public SimpleScorePlayer[] newArray(int size) {
            return new SimpleScorePlayer[size];
        }
    };
}