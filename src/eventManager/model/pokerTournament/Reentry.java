package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reentry {

    private int maxReentry = 0;
    private int cost;
    private int chips;
    private int maxLevel = 0;

    public Reentry(int cost, int chips) {
        this.cost = cost;
        this.chips = chips;
    }

    public Reentry() {
    }

    public int getMaxReentrada() {
        return maxReentry;
    }

    public void setMaxReentrada(int maxReentry) {
        this.maxReentry = maxReentry;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public String toString() {
        return "Max Reentries= " + maxReentry + ", cost= " + cost + ", chips= " + chips + ", Max level= " + maxLevel;
    }
}
