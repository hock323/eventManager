package eventManager.model.pokerTournament;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType
@XmlSeeAlso({SingularPlayer.class, AnonymousPlayer.class})
public abstract class Player {

    private int ID;
    private int addOns;
    private int reBuys;
    private int reentradas;

    public Player(int ID) {
        this.ID = ID;
    }

    public Player() {
    }

    @XmlAttribute
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @XmlAttribute
    public int getAddOns() {
        return addOns;
    }

    public void setAddOns(int addOns) {
        this.addOns = addOns;
    }

    @XmlAttribute
    public int getReBuys() {
        return reBuys;
    }

    public void setReBuys(int reBuys) {
        this.reBuys = reBuys;
    }

    @XmlAttribute
    public int getReentradas() {
        return reentradas;
    }

    public void setReentradas(int reentradas) {
        this.reentradas = reentradas;
    }

    public void addAddOn() {
        addOns++;
    }

    public void addReentrada() {
        reentradas++;
    }

    public void addRebuy() {
        reBuys++;
    }

    public void subtractRebuy() {
        reBuys--;
    }

    public void subtractAddOn() {
        addOns--;
    }

    public void subtractReentradas() {
        reentradas--;
    }

    @Override
    public String toString() {
        return "Player{" + "ID=" + ID + ", addOns=" + addOns + ", reBuys=" + reBuys + ", reentradas=" + reentradas + '}';
    }
}
