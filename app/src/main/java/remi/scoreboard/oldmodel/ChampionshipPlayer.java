package remi.scoreboard.oldmodel;

public class ChampionshipPlayer extends Player {

    private int victories = 0;
    private int defeats = 0;
    private int goalAverage = 0;
    private int gamesPlayed = 0;

    public ChampionshipPlayer(String name, int num) {
        super(name, num);
    }

    public int getVictories() {
        return victories;
    }

    public void addVictories() {
        this.victories++;
    }

    public int getDefeats() {
        return defeats;
    }

    public void addDefeats() {
        this.defeats++;
    }

    public int getGoalAverage() {
        return goalAverage;
    }

    public void addGoalAverage(int goalAverage) {
        this.goalAverage += goalAverage;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void addGamesPlayed() {
        this.gamesPlayed++;
    }
}
