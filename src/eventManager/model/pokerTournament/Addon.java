package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Addon {
    private int cost = 0;
    private int chips = 0;
    private int maxAddons;
    private int maxLevel;        
            
    public Addon() {
    }
    
    public Addon(int cost, int chips) {
        this.cost = cost;
        this.chips = chips;
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

    public int getMaxAddons() {
        return maxAddons;
    }

    public void setMaxAddons(int maxAddons) {
        this.maxAddons = maxAddons;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
    
    @Override
    public String toString() {
        return "cost= " + cost + ", chips=" + chips;
    }
    
}
