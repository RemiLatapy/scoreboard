package remi.scoreboard.model;

public class Player {

    private String name;
    private int num;

    public Player(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    /*
    ////// Parcelable implementation below
    private Player(Parcel in) {
        this.name = in.readString();
        this.num = in.readInt();
    }

    public static final Parcelable.Creator<Player> CREATOR
            = new Parcelable.Creator<Player>() {

        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.num);
    }
    */
}
