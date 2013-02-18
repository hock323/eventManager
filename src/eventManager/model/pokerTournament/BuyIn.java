package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleIntegerProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BuyIn {

    private SimpleIntegerProperty cost = new SimpleIntegerProperty();
    private SimpleIntegerProperty chips = new SimpleIntegerProperty();
    private SimpleIntegerProperty maxLevelBuyin = new SimpleIntegerProperty();

    public BuyIn() {
    }

    public BuyIn(int cost, int chips) {
        this.cost.set(cost);
        this.chips.set(chips);
    }

    @XmlElement
    public Integer getMaxLevelBuyin() {
        return maxLevelBuyin.get();
    }

    @XmlAttribute
    public int getCost() {
        return cost.get();
    }

    public void setCost(int cost) {
        this.cost.set(cost);
    }

    public void setMaxLevelBuyin(Integer maxLevelBuyin) {
        this.maxLevelBuyin.set(maxLevelBuyin);
    }

    @XmlAttribute
    public int getChips() {
        return chips.get();
    }

    public void setChips(int chips) {
        this.chips.set(chips);
    }

    public SimpleIntegerProperty costProperty() {
        return cost;
    }

    public SimpleIntegerProperty chipsProperty() {
        return chips;
    }

    public SimpleIntegerProperty maxLevelBuyinProperty() {
        return maxLevelBuyin;
    }

    @Override
    public String toString() {
        return "cost= " + cost.get() + ", chips= " + chips.get();
    }
}
