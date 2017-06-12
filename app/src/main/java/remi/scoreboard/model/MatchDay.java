package remi.scoreboard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MatchDay extends ArrayList<Match> implements Parcelable {
    public MatchDay()
    {
        super();
    }

    private MatchDay(Parcel in) {
        super(in.createTypedArrayList(Match.CREATOR));
    }

    public static final Creator<MatchDay> CREATOR = new Creator<MatchDay>() {
        @Override
        public MatchDay createFromParcel(Parcel in) {
            return new MatchDay(in);
        }

        @Override
        public MatchDay[] newArray(int size) {
            return new MatchDay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this);
    }
}
