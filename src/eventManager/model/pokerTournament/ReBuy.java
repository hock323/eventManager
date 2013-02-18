package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleIntegerProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Octavio
 */
@XmlRootElement
public class ReBuy {
    private SimpleIntegerProperty maxRebuy = new SimpleIntegerProperty();
    private SimpleIntegerProperty cost = new SimpleIntegerProperty();
    private SimpleIntegerProperty chips = new SimpleIntegerProperty();
    private SimpleIntegerProperty maxLevel = new SimpleIntegerProperty();

    public ReBuy(int cost, int chips) {
        this.cost.set(cost);
        this.chips.set(chips);
    }

    public ReBuy() {
    }

    public int getMaxRebuy() {
        return maxRebuy.get();
    }

    public void setMaxRebuy(int maxRebuy) {
        this.maxRebuy.set(maxRebuy);
    }

    public int getCost() {
        return cost.get();
    }

    public void setCost(int cost) {
        this.cost.set(cost);
    }

    public int getChips() {
        return chips.get();
    }

    public void setChips(int chips) {
        this.chips.set(chips);
    }

    public int getMaxLevel() {
        return maxLevel.get();
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel.set(maxLevel);
    }

    public SimpleIntegerProperty maxRebuyProperty() {
        return maxRebuy;
    }

    public SimpleIntegerProperty costProperty() {
        return cost;
    }

    public SimpleIntegerProperty chipsProperty() {
        return chips;
    }

    public SimpleIntegerProperty maxLevelProperty() {
        return maxLevel;
    }
    
    

    @Override
    public String toString() {
        return "Ma Rebuys= " + maxRebuy.get() + ", cost= " + cost.get() + ", chips= " + chips.get() + ", Max level= " + maxLevel.get() + '}';
    }
    
    
}
