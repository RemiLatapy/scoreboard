package remi.scoreboard.oldmodel;

public class ItemGameChooser {
    public int imageResId;
    private String gameTitle;
    private Class gameToLaunch;

    public ItemGameChooser(int imageResId, String gameTitle, Class gameToLaunch) {
        this.imageResId = imageResId;
        this.gameTitle = gameTitle;
        this.gameToLaunch = gameToLaunch;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public Class getGameToLaunch() {
        return gameToLaunch;
    }
}
