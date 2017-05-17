package remi.scoreboard.model;

public class PhaseDixPlayer extends Player {

    private int phase;
    private int points;

    public PhaseDixPlayer(String name, int num) {
        super(name, num);
        this.phase = 1;
        this.points = 0;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
