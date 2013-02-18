package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleIntegerProperty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChopPosition {

    private SimpleIntegerProperty position = new SimpleIntegerProperty();
    private SimpleIntegerProperty chips = new SimpleIntegerProperty();
    private SimpleIntegerProperty icm = new SimpleIntegerProperty();
    private SimpleIntegerProperty simple = new SimpleIntegerProperty();

    public ChopPosition() {
    }

    public ChopPosition(int position, int chips, int icm, int simple) {
        this.position.set(position);
        this.chips.set(chips);
        this.icm.set(icm);
        this.simple.set(simple);
    }

    public SimpleIntegerProperty positionProperty() {
        return position;
    }

    public int getPosition() {
        return position.get();
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public SimpleIntegerProperty chipsProperty() {
        return chips;
    }

    public int getChips() {
        return chips.get();
    }

    public void setChips(int chips) {
        this.chips.set(chips);
    }

    public int getIcm() {
        return icm.get();
    }

    public SimpleIntegerProperty icmProperty() {
        return icm;
    }

    public void setIcm(int icm) {
        this.icm.set(icm);
    }

    public SimpleIntegerProperty simpleProperty() {
        return simple;
    }

    public int getSimple() {
        return simple.get();
    }

    public void setSimple(int simple) {
        this.simple.set(simple);
    }
}
