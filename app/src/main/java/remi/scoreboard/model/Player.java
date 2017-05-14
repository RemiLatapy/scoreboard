package remi.scoreboard.model;

public class Player {

    private String name;
    private int num;
    private int phase;
    private int points;

    public Player(String name, int num) {
        this.name = name;
        this.num = num;
        this.phase = 1;
        this.points = 0;
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
