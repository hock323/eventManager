package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PrizePosition implements Cloneable {

    private final SimpleIntegerProperty position = new SimpleIntegerProperty();
    private final SimpleDoubleProperty absPercentage = new SimpleDoubleProperty();
    private final SimpleDoubleProperty relPercentage = new SimpleDoubleProperty();
    private final SimpleIntegerProperty money = new SimpleIntegerProperty();

    public PrizePosition() {
    }

    public PrizePosition(Integer position, Double absPercentage, Double relPercentage, Integer money) {
        this.position.set(position);
        this.absPercentage.set(absPercentage);
        this.relPercentage.set(relPercentage);
        this.money.set(money);
    }

    public SimpleIntegerProperty positionProperty() {
        return position;
    }

    public SimpleDoubleProperty absPercentageProperty() {
        return absPercentage;
    }

    public SimpleDoubleProperty relPercentageProperty() {
        return relPercentage;
    }

    public SimpleIntegerProperty moneyProperty() {
        return money;
    }

    @XmlElement
    public Double getAbsPercentage() {
        return absPercentage.get();
    }

    @XmlElement
    public Double getRelPercentage() {
        return relPercentage.get();
    }

    @XmlElement
    public int getMoney() {
        return money.get();
    }

    @XmlElement
    public Integer getPosition() {
        return position.get();
    }

    public void setAbsPercentage(Double absPercentage) {
        this.absPercentage.set(absPercentage);
    }

    public void setRelPercentage(Double relPercentage) {
        this.relPercentage.set(relPercentage);
    }

    public void setPosition(Integer position) {
        this.position.set(position);
    }

    public void setMoney(Integer money) {
        this.money.set(money);
    }

    @Override
    public String toString() {
        return "PrizePosition{" + "position=" + position + ", absPercentage=" + absPercentage + ", relPercentage=" + relPercentage + ", money=" + money + '}';
    }
}
