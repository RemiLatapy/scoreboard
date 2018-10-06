package remi.scoreboard.model;

import android.os.Parcel;

public final class PhaseDixPlayer extends SimpleScorePlayer {

    private int phase;

    public PhaseDixPlayer(String name, int num) {
        super(name, num);
        this.phase = 1;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void validPhase() {
        this.phase++;
    }

    ////// Parcelable implementation below
    private PhaseDixPlayer(Parcel in) {
        super(in);
        phase = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(phase);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhaseDixPlayer> CREATOR = new Creator<PhaseDixPlayer>() {
        @Override
        public PhaseDixPlayer createFromParcel(Parcel in) {
            return new PhaseDixPlayer(in);
        }

        @Override
        public PhaseDixPlayer[] newArray(int size) {
            return new PhaseDixPlayer[size];
        }
    };

    @Override
    public int compareTo(Player o) {
        int phaseRank = ((PhaseDixPlayer) o).getPhase() - phase;
        if (phaseRank != 0)
            return phaseRank;
        else
            return super.compareTo(o);
    }
}
