package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class Knockout {

    private int cost = 0;

    public Knockout() {
    }
    
    public Knockout(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
    
    
    
}
